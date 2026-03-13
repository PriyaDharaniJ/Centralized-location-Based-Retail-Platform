package com.retailplatform.controller;
import com.retailplatform.dto.ApiResponse;
import com.retailplatform.dto.ProductRequest;
import com.retailplatform.repository.CategoryRepository;
import com.retailplatform.repository.ProductRepository;
import com.retailplatform.repository.RetailOwnerRepository;
import com.retailplatform.service.ProductService;
import com.retailplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired private ProductService productService;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private RetailOwnerRepository ownerRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private JwtUtil jwtUtil;

    private Long getIdFromToken(String auth) {
        return jwtUtil.extractId(auth.substring(7));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(categoryRepo.findByIsActiveTrue()));
    }

    @GetMapping("/products/search")
    public ResponseEntity<?> searchProducts(@RequestParam String q,
                                             @RequestParam Double lat, @RequestParam Double lon,
                                             @RequestParam(defaultValue = "5") Double radius) {
        return ResponseEntity.ok(ApiResponse.ok(productService.searchProductsNearby(q, lat, lon, radius)));
    }

    @GetMapping("/shops/nearby")
    public ResponseEntity<?> getNearbyShops(@RequestParam Double lat, @RequestParam Double lon,
                                             @RequestParam(defaultValue = "5") Double radius) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getNearbyShops(lat, lon, radius)));
    }

    @GetMapping("/products/shop/{shopId}")
    public ResponseEntity<?> getShopProducts(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getOwnerProducts(shopId)));
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ownerRepo.findById(id).orElseThrow()));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(productRepo.findById(id).orElseThrow()));
    }

    // Retailer APIs
    @GetMapping("/retailer/products")
    public ResponseEntity<?> getMyProducts(@RequestHeader("Authorization") String auth) {
        Long id = getIdFromToken(auth);
        return ResponseEntity.ok(ApiResponse.ok(productService.getOwnerProducts(id)));
    }

    @PostMapping("/retailer/products")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String auth,
                                         @RequestBody ProductRequest req) {
        try {
            Long id = getIdFromToken(auth);
            return ResponseEntity.ok(ApiResponse.ok(productService.addProduct(id, req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/retailer/products/{productId}")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String auth,
                                            @PathVariable Long productId, @RequestBody ProductRequest req) {
        try {
            Long id = getIdFromToken(auth);
            return ResponseEntity.ok(ApiResponse.ok(productService.updateProduct(productId, id, req)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/retailer/products/{productId}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String auth,
                                            @PathVariable Long productId) {
        try {
            Long id = getIdFromToken(auth);
            productService.deleteProduct(productId, id);
            return ResponseEntity.ok(ApiResponse.ok("Product deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/retailer/profile")
    public ResponseEntity<?> getRetailerProfile(@RequestHeader("Authorization") String auth) {
        Long id = getIdFromToken(auth);
        return ResponseEntity.ok(ApiResponse.ok(ownerRepo.findById(id).orElseThrow()));
    }

    @PatchMapping("/retailer/shop-status")
    public ResponseEntity<?> updateShopStatus(@RequestHeader("Authorization") String auth,
                                               @RequestBody java.util.Map<String, Object> body) {
        Long id = getIdFromToken(auth);
        var owner = ownerRepo.findById(id).orElseThrow();
        if (body.containsKey("isOpen")) owner.setIsOpen((Boolean) body.get("isOpen"));
        if (body.containsKey("deliveryAvailable")) owner.setDeliveryAvailable((Boolean) body.get("deliveryAvailable"));
        return ResponseEntity.ok(ApiResponse.ok(ownerRepo.save(owner)));
    }
}
