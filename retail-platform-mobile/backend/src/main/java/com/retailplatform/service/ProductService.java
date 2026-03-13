package com.retailplatform.service;
import com.retailplatform.dto.NearbyShopDTO;
import com.retailplatform.dto.ProductRequest;
import com.retailplatform.entity.Product;
import com.retailplatform.entity.RetailOwner;
import com.retailplatform.repository.ProductRepository;
import com.retailplatform.repository.RetailOwnerRepository;
import com.retailplatform.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired private ProductRepository productRepo;
    @Autowired private RetailOwnerRepository ownerRepo;
    @Autowired private DistanceUtil distanceUtil;

    public Product addProduct(Long ownerId, ProductRequest req) {
        Product p = Product.builder()
                .retailOwnerId(ownerId).categoryId(req.getCategoryId())
                .name(req.getName()).description(req.getDescription())
                .price(req.getPrice()).originalPrice(req.getOriginalPrice())
                .quantity(req.getQuantity()).unit(req.getUnit())
                .brand(req.getBrand()).sku(req.getSku())
                .imageUrl(req.getImageUrl())
                .pickupAvailable(req.getPickupAvailable() != null ? req.getPickupAvailable() : true)
                .deliveryAvailable(req.getDeliveryAvailable() != null ? req.getDeliveryAvailable() : true)
                .discountPercent(req.getDiscountPercent() != null ? req.getDiscountPercent() : BigDecimal.ZERO)
                .isAvailable(true).soldCount(0).reviewCount(0).rating(BigDecimal.ZERO)
                .build();
        return productRepo.save(p);
    }

    public Product updateProduct(Long productId, Long ownerId, ProductRequest req) {
        Product p = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (!p.getRetailOwnerId().equals(ownerId)) throw new RuntimeException("Unauthorized");
        p.setName(req.getName()); p.setDescription(req.getDescription());
        p.setPrice(req.getPrice()); p.setOriginalPrice(req.getOriginalPrice());
        p.setQuantity(req.getQuantity()); p.setUnit(req.getUnit());
        p.setBrand(req.getBrand()); p.setSku(req.getSku());
        p.setImageUrl(req.getImageUrl());
        if (req.getCategoryId() != null) p.setCategoryId(req.getCategoryId());
        if (req.getPickupAvailable() != null) p.setPickupAvailable(req.getPickupAvailable());
        if (req.getDeliveryAvailable() != null) p.setDeliveryAvailable(req.getDeliveryAvailable());
        if (req.getDiscountPercent() != null) p.setDiscountPercent(req.getDiscountPercent());
        return productRepo.save(p);
    }

    public void deleteProduct(Long productId, Long ownerId) {
        Product p = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (!p.getRetailOwnerId().equals(ownerId)) throw new RuntimeException("Unauthorized");
        productRepo.delete(p);
    }

    public List<Product> getOwnerProducts(Long ownerId) {
        return productRepo.findByRetailOwnerId(ownerId);
    }

    public List<Map<String, Object>> searchProductsNearby(String query, Double userLat, Double userLon, Double radiusKm) {
        List<Product> products = productRepo.searchByName(query);
        List<RetailOwner> approvedOwners = ownerRepo.findAllApprovedActive();
        Map<Long, RetailOwner> ownerMap = approvedOwners.stream().collect(Collectors.toMap(RetailOwner::getId, o -> o));

        List<Map<String, Object>> results = new ArrayList<>();
        for (Product p : products) {
            RetailOwner owner = ownerMap.get(p.getRetailOwnerId());
            if (owner == null || !owner.getIsApproved()) continue;
            double dist = distanceUtil.calculateDistance(userLat, userLon,
                    owner.getLatitude().doubleValue(), owner.getLongitude().doubleValue());
            if (dist <= radiusKm) {
                Map<String, Object> item = new HashMap<>();
                item.put("product", p);
                item.put("shop", owner);
                item.put("distanceKm", Math.round(dist * 10.0) / 10.0);
                results.add(item);
            }
        }
        return results;
    }

    public List<NearbyShopDTO> getNearbyShops(Double userLat, Double userLon, Double radiusKm) {
        List<RetailOwner> owners = ownerRepo.findAllApprovedActive();
        List<NearbyShopDTO> result = new ArrayList<>();
        for (RetailOwner o : owners) {
            double dist = distanceUtil.calculateDistance(userLat, userLon,
                    o.getLatitude().doubleValue(), o.getLongitude().doubleValue());
            if (dist <= radiusKm) {
                result.add(new NearbyShopDTO(o.getId(), o.getShopName(), o.getShopAddress(),
                        o.getShopCategory(), o.getLatitude(), o.getLongitude(),
                        o.getIsOpen(), o.getDeliveryAvailable(), o.getRating(),
                        o.getTotalOrders(), o.getShopImage(), o.getPhone(),
                        Math.round(dist * 10.0) / 10.0));
            }
        }
        result.sort(Comparator.comparingDouble(NearbyShopDTO::getDistanceKm));
        return result;
    }
}
