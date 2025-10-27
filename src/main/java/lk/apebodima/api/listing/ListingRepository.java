package lk.apebodima.api.listing;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {
    // You can add custom query methods here later if needed
}