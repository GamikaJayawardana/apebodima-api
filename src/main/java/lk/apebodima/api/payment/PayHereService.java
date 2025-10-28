package lk.apebodima.api.payment;

import java.util.Map;

public interface PayHereService {
    Map<String, String> initiatePayment(PaymentRequestDto paymentRequestDto);
    boolean processNotify(Map<String, String> notification);
}