package lk.apebodima.api.listing;

import jakarta.persistence.*;
import lk.apebodima.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Core Details ---
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private BigDecimal rentAmount; // Monthly rent

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType; // e.g., HOUSE, APARTMENT, ROOM

    // --- Location ---
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    // --- Property Specifications ---
    private Integer bedrooms;
    private Integer bathrooms;
    private Double sizeSqFt; // Size in square feet

    // --- Amenities (Using a simple list for now) ---
    @ElementCollection
    @CollectionTable(name = "listing_amenities", joinColumns = @JoinColumn(name = "listing_id"))
    @Column(name = "amenity")
    private List<String> amenities; // e.g., ["A/C", "Hot Water", "Parking"]

    // --- Listing Management ---
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ListingStatus status = ListingStatus.AVAILABLE; // e.g., AVAILABLE, RENTED

    private LocalDate availableFrom; // Date the property is available

    @Builder.Default
    private boolean isBoosted = false;

    // --- Timestamps for tracking ---
    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false)
    private User landlord;
}



