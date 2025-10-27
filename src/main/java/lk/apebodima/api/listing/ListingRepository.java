package lk.apebodima.api.listing;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
// Extend MongoRepository and use String for the ID type
public interface ListingRepository extends MongoRepository<Listing, String> {
    // You can add custom query methods here later if needed
}