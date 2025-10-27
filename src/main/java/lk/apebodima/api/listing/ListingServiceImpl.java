package lk.apebodima.api.listing;

import lk.apebodima.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public ListingDto createListing(CreateListingRequest request, List<MultipartFile> images) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = imageUploadService.uploadImage(image);
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed", e);
                }
            }
        }

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
                .landlordId(currentUser.getId())
                .imageUrls(imageUrls)
                .status(ListingStatus.AVAILABLE)
                .build();

        Listing savedListing = listingRepository.save(listing);
        return mapToListingDto(savedListing);
    }

    @Override
    public ListingDto getListingById(String id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));
        return mapToListingDto(listing);
    }

    @Override
    public ListingDto updateListing(String id, UpdateListingRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to update this listing.");
        }

        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        //... map other fields from request ...
        Listing updatedListing = listingRepository.save(listing);
        return mapToListingDto(updatedListing);
    }

    @Override
    public void deleteListing(String id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this listing.");
        }
        listingRepository.delete(listing);
    }

    @Override
    public Page<ListingDto> searchListings(String city, PropertyType propertyType, BigDecimal minRent, BigDecimal maxRent, Integer minBedrooms, Pageable pageable) {
        Page<Listing> listingPage = listingRepository.findListingsByCriteria(
                city, propertyType, minRent, maxRent, minBedrooms, pageable);
        return listingPage.map(this::mapToListingDto);
    }

    @Override
    public List<ListingDto> getMyListings() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return listingRepository.findByLandlordId(currentUser.getId())
                .stream()
                .map(this::mapToListingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ListingDto boostListing(String id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to boost this listing.");
        }
        listing.setBoosted(true);
        Listing savedListing = listingRepository.save(listing);
        return mapToListingDto(savedListing);
    }

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
                .imageUrls(listing.getImageUrls())
                .status(listing.getStatus())
                .availableFrom(listing.getAvailableFrom())
                .isBoosted(listing.isBoosted())
                .landlordId(listing.getLandlordId())
                .createdAt(listing.getCreatedAt())
                .build();
    }
}