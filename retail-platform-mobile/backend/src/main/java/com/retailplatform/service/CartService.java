package com.retailplatform.service;
import com.retailplatform.entity.Cart;
import com.retailplatform.entity.Product;
import com.retailplatform.repository.CartRepository;
import com.retailplatform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CartService {
    @Autowired private CartRepository cartRepo;
    @Autowired private ProductRepository productRepo;

    public List<Map<String, Object>> getCart(Long userId) {
        List<Cart> carts = cartRepo.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cart c : carts) {
            productRepo.findById(c.getProductId()).ifPresent(p -> {
                Map<String, Object> item = new HashMap<>();
                item.put("cartId", c.getId());
                item.put("quantity", c.getQuantity());
                item.put("product", p);
                result.add(item);
            });
        }
        return result;
    }

    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        Optional<Cart> existing = cartRepo.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            Cart c = existing.get();
            c.setQuantity(c.getQuantity() + quantity);
            return cartRepo.save(c);
        }
        return cartRepo.save(Cart.builder().userId(userId).productId(productId).quantity(quantity).build());
    }

    public Cart updateCart(Long userId, Long productId, Integer quantity) {
        Cart c = cartRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        c.setQuantity(quantity);
        return cartRepo.save(c);
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartRepo.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void clearCart(Long userId) { cartRepo.deleteByUserId(userId); }
}
