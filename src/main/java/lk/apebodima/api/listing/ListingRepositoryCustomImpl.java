package lk.apebodima.api.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListingRepositoryCustomImpl implements ListingRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Listing> findListingsByCriteria(
            String city, PropertyType propertyType, BigDecimal minRent,
            BigDecimal maxRent, Integer minBedrooms, Pageable pageable) {

        // Add a primary sort rule: boosted listings first, then by the user's requested sort.
        Sort sortByBoost = Sort.by(Sort.Direction.DESC, "isBoosted");

        // 2. Combine the boost sort with the user's requested sort
        Sort finalSort = sortByBoost.and(pageable.getSort());

        // 3. Create a new Pageable object with the combined sort
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);

        // 4. Build the query with the new, correctly sorted Pageable
        Query query = new Query().with(sortedPageable);

        List<Criteria> criteria = new ArrayList<>();

        if (city != null && !city.isEmpty()) {
            criteria.add(Criteria.where("city").is(city));
        }
        if (propertyType != null) {
            criteria.add(Criteria.where("propertyType").is(propertyType));
        }
        if (minRent != null) {
            criteria.add(Criteria.where("rentAmount").gte(minRent));
        }
        if (maxRent != null) {
            criteria.add(Criteria.where("rentAmount").lte(maxRent));
        }
        if (minBedrooms != null) {
            criteria.add(Criteria.where("bedrooms").gte(minBedrooms));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        List<Listing> listings = mongoTemplate.find(query, Listing.class);
        return PageableExecutionUtils.getPage(
                listings,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Listing.class)
        );
    }
}