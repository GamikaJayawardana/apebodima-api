package lk.apebodima.api.listing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ListingService {

    ListingDto createListing(CreateListingRequest request, List<MultipartFile> images);

    ListingDto getListingById(String id);

    ListingDto updateListing(String id, UpdateListingRequest request);

    void deleteListing(String id);

    Page<ListingDto> searchListings(String city, PropertyType propertyType, BigDecimal minRent, BigDecimal maxRent, Integer minBedrooms, Pageable pageable);

    List<ListingDto> getMyListings();

    Page<ListingDto> searchByLocation(GeoJsonPoint point, Distance distance, Pageable pageable);

    Map<String, Object> boostListing(String id, BoostRequestDto boostRequest);
}