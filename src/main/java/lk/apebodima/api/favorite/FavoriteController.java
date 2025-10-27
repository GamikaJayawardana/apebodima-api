// In: src/main/java/lk/apebodima/api/favorite/FavoriteController.java
package lk.apebodima.api.favorite;

import lk.apebodima.api.listing.ListingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{listingId}")
    public ResponseEntity<Void> addFavorite(@PathVariable String listingId) {
        favoriteService.addFavorite(listingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable String listingId) {
        favoriteService.removeFavorite(listingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ListingDto>> getMyFavorites() {
        return ResponseEntity.ok(favoriteService.getMyFavorites());
    }
}