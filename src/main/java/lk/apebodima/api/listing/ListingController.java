package lk.apebodima.api.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    @PreAuthorize("hasAuthority('LANDLORD')") // Secure this endpoint
    public ResponseEntity<ListingDto> createListing(@RequestBody CreateListingRequest request) {
        ListingDto createdListing = listingService.createListing(request);
        return new ResponseEntity<>(createdListing, HttpStatus.CREATED);
    }
}