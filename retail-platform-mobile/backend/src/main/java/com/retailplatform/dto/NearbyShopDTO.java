package com.retailplatform.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data @AllArgsConstructor
public class NearbyShopDTO {
    private Long id;
    private String shopName;
    private String shopAddress;
    private String shopCategory;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isOpen;
    private Boolean deliveryAvailable;
    private BigDecimal rating;
    private Integer totalOrders;
    private String shopImage;
    private String phone;
    private Double distanceKm;
}
