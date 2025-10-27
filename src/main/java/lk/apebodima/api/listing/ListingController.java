package lk.apebodima.api.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority('LANDLORD')")
    public ResponseEntity<ListingDto> createListing(
            @RequestPart("listing") CreateListingRequest request,
            @RequestPart("images") List<MultipartFile> images) {
        ListingDto createdListing = listingService.createListing(request, images);
        return new ResponseEntity<>(createdListing, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListingDto>> searchListings(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<ListingDto> listingPage = listingService.searchListings(
                city, propertyType, minRent, maxRent, minBedrooms, pageable);
        return ResponseEntity.ok(listingPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LANDLORD')")
    public ResponseEntity<ListingDto> updateListing(@PathVariable String id, @RequestBody UpdateListingRequest request) {
        return ResponseEntity.ok(listingService.updateListing(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LANDLORD')")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-listings")
    @PreAuthorize("hasAuthority('LANDLORD')")
    public ResponseEntity<List<ListingDto>> getMyListings() {
        return ResponseEntity.ok(listingService.getMyListings());
    }

    @PostMapping("/{id}/boost")
    @PreAuthorize("hasAuthority('LANDLORD')")
    public ResponseEntity<ListingDto> boostListing(@PathVariable String id) {
        return ResponseEntity.ok(listingService.boostListing(id));
    }
}