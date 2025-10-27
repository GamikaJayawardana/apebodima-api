package lk.apebodima.api.listing;

import lk.apebodima.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;

    @Override
    public ListingDto createListing(CreateListingRequest request) {
        //Get the currently authenticated user (the landlord)
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Build the Listing object from the request
        Listing listing = Listing.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .rentAmount(request.getRentAmount())
                .propertyType(request.getPropertyType())
                .address(request.getAddress())
                .city(request.getCity())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .sizeSqFt(request.getSizeSqFt())
                .amenities(request.getAmenities())
                .availableFrom(request.getAvailableFrom())
                .isBoosted(request.isBoosted())
                .landlordId(currentUser.getId()) // Link to the current user
                .status(ListingStatus.AVAILABLE) // Default status
                .build();

        //Save the new listing to the database
        Listing savedListing = listingRepository.save(listing);

        //Map the saved entity to a DTO and return it
        return mapToListingDto(savedListing);
    }

    // Helper method to map a Listing entity to a ListingDto
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