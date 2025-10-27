// In: src/main/java/lk/apebodima/api/favorite/FavoriteServiceImpl.java
package lk.apebodima.api.favorite;

import lk.apebodima.api.listing.Listing;
import lk.apebodima.api.listing.ListingDto;
import lk.apebodima.api.listing.ListingRepository;
import lk.apebodima.api.user.User;
import lk.apebodima.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    @Override
    public void addFavorite(String listingId) {
        User currentUser = getCurrentUser();
        // Check if the listing actually exists
        listingRepository.findById(listingId).orElseThrow(() -> new RuntimeException("Listing not found"));

        currentUser.getFavoriteListingIds().add(listingId);
        userRepository.save(currentUser);
    }

    @Override
    public void removeFavorite(String listingId) {
        User currentUser = getCurrentUser();
        currentUser.getFavoriteListingIds().remove(listingId);
        userRepository.save(currentUser);
    }

    @Override
    public List<ListingDto> getMyFavorites() {
        User currentUser = getCurrentUser();
        List<Listing> favoriteListings = listingRepository.findAllById(currentUser.getFavoriteListingIds());
        return favoriteListings.stream().map(this::mapToListingDto).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // You can move the mapToListingDto method to a shared utility class later,
    // but for now, duplicating it here is fine to get the feature working.
    private ListingDto mapToListingDto(Listing listing) {
        return ListingDto.builder()
                .id(listing.getId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .rentAmount(listing.getRentAmount())
                .propertyType(listing.getPropertyType())
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
                .build();
    }
}