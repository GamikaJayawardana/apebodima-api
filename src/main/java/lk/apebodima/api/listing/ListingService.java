package lk.apebodima.api.listing;

import java.util.List;

public interface ListingService {
    ListingDto createListing(CreateListingRequest request);
    List<ListingDto> getAllListings();
    ListingDto getListingById(String id);
    ListingDto updateListing(String id, UpdateListingRequest request);
    void deleteListing(String id);
}
