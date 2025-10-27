package lk.apebodima.api.listing;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String>, ListingRepositoryCustom {
    List<Listing> findByLandlordId(String landlordId);
}