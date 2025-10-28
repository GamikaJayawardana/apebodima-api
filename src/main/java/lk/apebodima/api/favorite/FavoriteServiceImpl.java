// In: src/main/java/lk/apebodima/api/favorite/FavoriteServiceImpl.java
package lk.apebodima.api.favorite;

import lk.apebodima.api.listing.Listing;
import lk.apebodima.api.listing.ListingDto;
import lk.apebodima.api.listing.ListingMapper;
import lk.apebodima.api.listing.ListingRepository;
import lk.apebodima.api.shared.exception.ResourceNotFoundException;
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
    private final ListingMapper listingMapper;

    @Override
    public void addFavorite(String listingId) {
        User currentUser = getCurrentUser();
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + listingId));

        if (currentUser.getFavoriteListingIds().add(listingId)) {
            listing.setFavoriteCount(listing.getFavoriteCount() + 1);
            listingRepository.save(listing);
            userRepository.save(currentUser);
        }
    }

    @Override
    public void removeFavorite(String listingId) {
        User currentUser = getCurrentUser();
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + listingId));

        if (currentUser.getFavoriteListingIds().remove(listingId)) {
            listing.setFavoriteCount(listing.getFavoriteCount() - 1);
            listingRepository.save(listing);
            userRepository.save(currentUser);
        }
    }

    @Override
    public List<ListingDto> getMyFavorites() {
        User currentUser = getCurrentUser();
        List<Listing> favoriteListings = listingRepository.findAllById(currentUser.getFavoriteListingIds());
        return favoriteListings.stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
    }
}