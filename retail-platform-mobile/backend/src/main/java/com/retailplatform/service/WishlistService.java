package com.retailplatform.service;
import com.retailplatform.entity.Wishlist;
import com.retailplatform.entity.Product;
import com.retailplatform.repository.WishlistRepository;
import com.retailplatform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class WishlistService {
    @Autowired private WishlistRepository wishlistRepo;
    @Autowired private ProductRepository productRepo;

    public List<Map<String, Object>> getWishlist(Long userId) {
        List<Wishlist> wishlists = wishlistRepo.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Wishlist w : wishlists) {
            productRepo.findById(w.getProductId()).ifPresent(p -> {
                Map<String, Object> item = new HashMap<>();
                item.put("wishlistId", w.getId());
                item.put("product", p);
                result.add(item);
            });
        }
        return result;
    }

    public boolean toggleWishlist(Long userId, Long productId) {
        if (wishlistRepo.existsByUserIdAndProductId(userId, productId)) {
            wishlistRepo.deleteByUserIdAndProductId(userId, productId);
            return false;
        } else {
            wishlistRepo.save(Wishlist.builder().userId(userId).productId(productId).build());
            return true;
        }
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepo.existsByUserIdAndProductId(userId, productId);
    }
}
