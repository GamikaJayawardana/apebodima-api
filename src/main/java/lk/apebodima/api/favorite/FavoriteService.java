package lk.apebodima.api.favorite;

import lk.apebodima.api.listing.ListingDto;
import java.util.List;

public interface FavoriteService {
    void addFavorite(String listingId);
    void removeFavorite(String listingId);
    List<ListingDto> getMyFavorites();
}