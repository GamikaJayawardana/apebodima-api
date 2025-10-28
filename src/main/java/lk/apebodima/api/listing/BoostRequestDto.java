package lk.apebodima.api.listing;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BoostRequestDto {
    private BigDecimal amount;
    private String currency;
}