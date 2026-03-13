package com.retailplatform.dto;
import lombok.Data;
import java.util.List;
@Data
public class OrderRequest {
    private Long retailOwnerId;
    private String orderType; // DELIVERY or PICKUP
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private String customerPhone;
    private String notes;
    private String paymentMethod;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
