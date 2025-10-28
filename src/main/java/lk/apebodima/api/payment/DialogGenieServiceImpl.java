package lk.apebodima.api.payment;

import lk.apebodima.api.listing.Listing;
import lk.apebodima.api.listing.ListingRepository;
import lk.apebodima.api.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils; // Or another library as required by Genie

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class DialogGenieServiceImpl implements DialogGenieService {

    @Value("${dialog.genie.api.key}")
    private String apiKey;

    @Value("${dialog.genie.api.secret}")
    private String apiSecret;

    @Value("${dialog.genie.notify.url}")
    private String notifyUrl;

    @Value("${dialog.genie.return.url}")
    private String returnUrl;

    private final PaymentRepository paymentRepository;
    private final ListingRepository listingRepository;

    public DialogGenieServiceImpl(PaymentRepository paymentRepository, ListingRepository listingRepository) {
        this.paymentRepository = paymentRepository;
        this.listingRepository = listingRepository;
    }

    @Override
    public Map<String, Object> initiatePayment(PaymentRequestDto paymentRequestDto) {
        String amountFormatted = String.format("%.2f", paymentRequestDto.getAmount());

        // IMPORTANT: Replace this with the signature generation logic from Dialog Genie's documentation
        String signature = generateSignature(paymentRequestDto.getOrderId(), amountFormatted);

        Map<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("apiKey", apiKey);
        paymentDetails.put("orderId", paymentRequestDto.getOrderId());
        paymentDetails.put("amount", amountFormatted);
        paymentDetails.put("currency", paymentRequestDto.getCurrency());
        paymentDetails.put("description", paymentRequestDto.getItems());
        paymentDetails.put("notifyUrl", notifyUrl);
        paymentDetails.put("returnUrl", returnUrl);
        paymentDetails.put("signature", signature);
        // Add any other required parameters like customer details here

        return paymentDetails;
    }

    @Override
    public boolean processNotify(Map<String, String> notification) {
        String orderId = notification.get("orderId");
        String statusCode = notification.get("statusCode"); // Use the actual parameter name from Genie
        String receivedSignature = notification.get("signature"); // Use the actual parameter name from Genie

        // IMPORTANT: Replace this with the signature validation logic from Dialog Genie's documentation
        if (validateSignature(notification, receivedSignature)) {
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));

            if ("SUCCESS".equalsIgnoreCase(statusCode)) { // Use the actual success status from Genie
                payment.setStatus(PaymentStatus.SUCCESS);

                Listing listing = listingRepository.findById(payment.getListingId())
                        .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + payment.getListingId()));

                listing.setBoosted(true);
                listingRepository.save(listing);
                System.out.println("Genie Payment successful and listing boosted for order: " + orderId);

            } else {
                payment.setStatus(PaymentStatus.FAILED);
                System.out.println("Genie Payment failed for order: " + orderId);
            }
            paymentRepository.save(payment);
            return true;
        } else {
            System.out.println("Genie signature mismatch for order: " + orderId);
            return false;
        }
    }

    // --- PLACEHOLDER METHODS ---
    // YOU MUST REPLACE THESE WITH THE OFFICIAL DIALOG GENIE ALGORITHM

    private String generateSignature(String orderId, String amount) {
        // Example logic (NOT REAL): String data = orderId + amount + apiSecret;
        // return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
        System.err.println("WARNING: Using placeholder signature generation. Replace with Dialog Genie's official algorithm.");
        return "dummy-signature";
    }

    private boolean validateSignature(Map<String, String> notification, String receivedSignature) {
        // Example logic (NOT REAL): Reconstruct the signature from the notification data and your secret key
        // and compare it with the receivedSignature.
        System.err.println("WARNING: Using placeholder signature validation. Replace with Dialog Genie's official algorithm.");
        return true;
    }
}