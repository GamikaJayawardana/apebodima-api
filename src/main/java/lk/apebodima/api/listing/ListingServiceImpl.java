package lk.apebodima.api.listing;

import lk.apebodima.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ListingDto> getAllListings() {
        return listingRepository.findAll() // Fetch all listings from MongoDB
                .stream()
                .map(this::mapToListingDto) // Convert each one to a DTO
                .collect(Collectors.toList());
    }

    @Override
    public ListingDto getListingById(String id) {
        Listing listing = listingRepository.findById(id)
                // Throw an exception if no listing with that ID is found
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));
        return mapToListingDto(listing);
    }

    @Override
    public ListingDto updateListing(String id, UpdateListingRequest request) {
        // Get the current user
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Find the listing to update
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));

        //!! CRITICAL SECURITY CHECK !!
        // Ensure the user trying to update the listing is the one who created it.
        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to update this listing.");
        }

        //Update the fields
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setRentAmount(request.getRentAmount());
        listing.setPropertyType(request.getPropertyType());
        listing.setAddress(request.getAddress());
        listing.setCity(request.getCity());
        listing.setBedrooms(request.getBedrooms());
        listing.setBathrooms(request.getBathrooms());
        listing.setSizeSqFt(request.getSizeSqFt());
        listing.setAmenities(request.getAmenities());
        listing.setStatus(request.getStatus());
        listing.setAvailableFrom(request.getAvailableFrom());
        listing.setBoosted(request.isBoosted());

        //Save the updated listing and return the DTO
        Listing updatedListing = listingRepository.save(listing);
        return mapToListingDto(updatedListing);
    }

    @Override
    public void deleteListing(String id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));

        // !! CRITICAL SECURITY CHECK !!
        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this listing.");
        }

        listingRepository.delete(listing);
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