package lk.apebodima.api.listing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ListingRepositoryCustom {
    Page<Listing> findListingsByCriteria(
            String city,
            PropertyType propertyType,
            BigDecimal minRent,
            BigDecimal maxRent,
            Integer minBedrooms,
            Pageable pageable
    );
}