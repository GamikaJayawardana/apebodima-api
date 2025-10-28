package lk.apebodima.api.payment;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    private String listingId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String items;
}