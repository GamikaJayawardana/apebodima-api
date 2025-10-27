// In: src/main/java/lk/apebodima/api/listing/Listing.java
package lk.apebodima.api.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "listings")
public class Listing {

    @Id
    private String id;

    // --- Core Details ---
    private String title;
    private String description;
    private BigDecimal rentAmount;
    private PropertyType propertyType;

    private List<String> imageUrls;

    // --- Location ---
    private String address;
    private String city;

    // --- Property Specifications ---
    private Integer bedrooms;
    private Integer bathrooms;
    private Double sizeSqFt;
    private List<String> amenities;

    // --- Listing Management ---
    @Builder.Default
    private ListingStatus status = ListingStatus.AVAILABLE;
    private LocalDate availableFrom;
    @Builder.Default
    private boolean isBoosted = false;

    // --- Relationships ---
    private String landlordId; // Store the ID of the landlord user

    // --- Timestamps ---
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}