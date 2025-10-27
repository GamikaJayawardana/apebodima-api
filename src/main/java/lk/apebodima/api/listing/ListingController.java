package lk.apebodima.api.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
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
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
    }
}