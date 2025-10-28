package lk.apebodima.api.payment;

import lk.apebodima.api.listing.Listing;
import lk.apebodima.api.listing.ListingRepository;
import lk.apebodima.api.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Formatter;


@Service
public class PayHereServiceImpl implements PayHereService {

    @Value("${payhere.merchant-id}")
    private String merchantId;

    @Value("${payhere.merchant-secret}")
    private String merchantSecret;

    @Value("${payhere.notify-url}")
    private String notifyUrl;

    @Value("${payhere.return-url}")
    private String returnUrl;

    @Value("${payhere.cancel-url}")
    private String cancelUrl;

    private final ListingRepository listingRepository;
    private final PaymentRepository paymentRepository;

    public PayHereServiceImpl(ListingRepository listingRepository, PaymentRepository paymentRepository) {
        this.listingRepository = listingRepository;
        this.paymentRepository = paymentRepository;
    }


    @Override
    public Map<String, String> initiatePayment(PaymentRequestDto paymentRequestDto) {
        // You can fetch user details from SecurityContext if needed
        String customerFirstName = "John"; // Replace with actual user data
        String customerLastName = "Doe";
        String customerEmail = "john.doe@example.com";
        String customerPhone = "0771234567";

        String amountFormatted = String.format("%.2f", paymentRequestDto.getAmount());

        String hash = generateHash(paymentRequestDto.getOrderId(), amountFormatted, paymentRequestDto.getCurrency());

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("merchant_id", merchantId);
        paymentDetails.put("return_url", returnUrl);
        paymentDetails.put("cancel_url", cancelUrl);
        paymentDetails.put("notify_url", notifyUrl);
        paymentDetails.put("first_name", customerFirstName);
        paymentDetails.put("last_name", customerLastName);
        paymentDetails.put("email", customerEmail);
        paymentDetails.put("phone", customerPhone);
        paymentDetails.put("address", "No.1, Galle Road"); // Replace with actual user data
        paymentDetails.put("city", "Colombo");
        paymentDetails.put("country", "Sri Lanka");
        paymentDetails.put("order_id", paymentRequestDto.getOrderId());
        paymentDetails.put("items", paymentRequestDto.getItems());
        paymentDetails.put("currency", paymentRequestDto.getCurrency());
        paymentDetails.put("amount", amountFormatted);
        paymentDetails.put("hash", hash);

        return paymentDetails;
    }

    @Override
    public boolean processNotify(Map<String, String> notification) {
        String merchantId = notification.get("merchant_id");
        String orderId = notification.get("order_id");
        String payhereAmount = notification.get("payhere_amount");
        String payhereCurrency = notification.get("payhere_currency");
        String statusCode = notification.get("status_code");
        String md5sig = notification.get("md5sig");

        // IMPORTANT: Validate the MD5 signature
        String localMd5sig = generateNotifyHash(orderId, payhereAmount, payhereCurrency, statusCode);

        if (localMd5sig.equalsIgnoreCase(md5sig)) {
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));

            if ("2".equals(statusCode)) { // 2 = SUCCESS
                payment.setStatus(PaymentStatus.SUCCESS);

                Listing listing = listingRepository.findById(payment.getListingId())
                        .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + payment.getListingId()));

                listing.setBoosted(true);
                listingRepository.save(listing);
                System.out.println("Payment successful and listing boosted for order: " + orderId);

            } else { // Handle other statuses (0, -1, -2, -3)
                payment.setStatus(PaymentStatus.FAILED);
                System.out.println("Payment failed for order: " + orderId);
            }
            paymentRepository.save(payment);
            return true;
        } else {
            System.out.println("Hash mismatch for order: " + orderId);
            return false;
        }
    }


    private String generateHash(String orderId, String amount, String currency) {
        String amountFormatted = new BigDecimal(amount).setScale(2).toString();
        String secret = DigestUtils.md5DigestAsHex(merchantSecret.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        String data = merchantId + orderId + amountFormatted + currency + secret;
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

    private String generateNotifyHash(String orderId, String amount, String currency, String statusCode) {
        String secret = DigestUtils.md5DigestAsHex(merchantSecret.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        String data = merchantId + orderId + amount + currency + statusCode + secret;
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }
}