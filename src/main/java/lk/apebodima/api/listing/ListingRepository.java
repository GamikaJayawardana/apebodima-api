package lk.apebodima.api.listing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String>, ListingRepositoryCustom {
    List<Listing> findByLandlordId(String landlordId);
    List<Listing> findAllByIsBoostedIsTrue();
    Page<Listing> findByLocationNear(GeoJsonPoint location, Distance distance, Pageable pageable);
}