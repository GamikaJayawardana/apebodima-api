package lk.apebodima.api.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateListingRequest {
    private String title;
    private String description;
    private BigDecimal rentAmount;
    private PropertyType propertyType;
    private String address;
    private String city;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double sizeSqFt;
    private List<String> amenities;
    private ListingStatus status; // Allow changing the status (e.g., to RENTED)
    private LocalDate availableFrom;
    private boolean isBoosted;
}