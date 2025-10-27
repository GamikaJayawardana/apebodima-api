// In: src/main/java/lk/apebodima/api/listing/ListingServiceImpl.java
package lk.apebodima.api.listing;

import lk.apebodima.api.shared.exception.ResourceNotFoundException;
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
    private final ListingMapper listingMapper;

    @Override
    public ListingDto createListing(CreateListingRequest request, List<MultipartFile> images) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    imageUrls.add(imageUploadService.uploadImage(image));
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
        return listingMapper.toDto(savedListing);
    }

    @Override
    public ListingDto getListingById(String id) {
        return listingRepository.findById(id)
                .map(listingMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
    }

    @Override
    public ListingDto updateListing(String id, UpdateListingRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to update this listing.");
        }

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

        Listing updatedListing = listingRepository.save(listing);
        return listingMapper.toDto(updatedListing);
    }

    @Override
    public void deleteListing(String id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this listing.");
        }
        listingRepository.delete(listing);
    }

    @Override
    public Page<ListingDto> searchListings(String city, PropertyType propertyType, BigDecimal minRent, BigDecimal maxRent, Integer minBedrooms, Pageable pageable) {
        Page<Listing> listingPage = listingRepository.findListingsByCriteria(
                city, propertyType, minRent, maxRent, minBedrooms, pageable);
        return listingPage.map(listingMapper::toDto);
    }

    @Override
    public List<ListingDto> getMyListings() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return listingRepository.findByLandlordId(currentUser.getId())
                .stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ListingDto boostListing(String id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));

        if (!listing.getLandlordId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to boost this listing.");
        }
        listing.setBoosted(true);
        Listing savedListing = listingRepository.save(listing);
        return listingMapper.toDto(savedListing);
    }
}