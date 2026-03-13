package com.retailplatform.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductRequest {
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer quantity;
    private String unit;
    private String brand;
    private String sku;
    private String imageUrl;
    private Boolean pickupAvailable;
    private Boolean deliveryAvailable;
    private BigDecimal discountPercent;
}
