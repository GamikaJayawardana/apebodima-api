// Create this new file in: src/main/java/lk/apebodima/api/listing/ListingMapper.java
package lk.apebodima.api.listing;

import org.springframework.stereotype.Component;

@Component
public class ListingMapper {

    public ListingDto toDto(Listing listing) {
        if (listing == null) {
            return null;
        }

        return ListingDto.builder()
                .id(listing.getId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .rentAmount(listing.getRentAmount())
                .propertyType(listing.getPropertyType())
                .imageUrls(listing.getImageUrls())
                .address(listing.getAddress())
                .city(listing.getCity())
                .bedrooms(listing.getBedrooms())
                .bathrooms(listing.getBathrooms())
                .sizeSqFt(listing.getSizeSqFt())
                .amenities(listing.getAmenities())
                .status(listing.getStatus())
                .availableFrom(listing.getAvailableFrom())
                .isBoosted(listing.isBoosted())
                .landlordId(listing.getLandlordId())
                .createdAt(listing.getCreatedAt())
                .viewCount(listing.getViewCount())
                .favoriteCount(listing.getFavoriteCount())
                .build();
    }
}