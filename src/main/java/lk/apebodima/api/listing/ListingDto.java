package lk.apebodima.api.listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingDto {
    private String id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;
    private String description;
    private BigDecimal rentAmount;
    private PropertyType propertyType;
    private List<String> imageUrls;
    private String address;
    private String city;
    private Integer bedrooms;
    private Integer bathrooms;
    private long viewCount;
    private long favoriteCount;
    private Double sizeSqFt;
    private List<String> amenities;
    private ListingStatus status;
    private LocalDate availableFrom;
    private boolean isBoosted;
    private String landlordId;
    private Instant createdAt;
}