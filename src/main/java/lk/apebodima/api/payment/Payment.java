package lk.apebodima.api.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;
    private String orderId;
    private String listingId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
}