package lk.apebodima.api.payment;

import java.util.Map;

public interface DialogGenieService {
    Map<String, Object> initiatePayment(PaymentRequestDto paymentRequestDto);
    boolean processNotify(Map<String, String> notification);
}