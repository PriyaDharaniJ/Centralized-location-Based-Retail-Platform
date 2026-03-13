package com.retailplatform.service;
import com.retailplatform.dto.OrderRequest;
import com.retailplatform.entity.*;
import com.retailplatform.repository.*;
import com.retailplatform.util.TrackingIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private RetailOwnerRepository ownerRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private TrackingIdGenerator trackingIdGen;
    @Autowired private NotificationService notifService;

    @Transactional
    public Order placeOrder(Long userId, OrderRequest req) {
        RetailOwner owner = ownerRepo.findById(req.getRetailOwnerId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        if (!owner.getIsOpen()) throw new RuntimeException("Shop is currently closed");
        if ("DELIVERY".equals(req.getOrderType()) && !owner.getDeliveryAvailable())
            throw new RuntimeException("Delivery not available for this shop");

        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderRequest.OrderItemRequest itemReq : req.getItems()) {
            Product p = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            if (p.getQuantity() < itemReq.getQuantity())
                throw new RuntimeException("Insufficient stock for: " + p.getName());
            BigDecimal total = p.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            subtotal = subtotal.add(total);
            items.add(OrderItem.builder()
                    .productId(p.getId()).productName(p.getName()).productImage(p.getImageUrl())
                    .quantity(itemReq.getQuantity()).price(p.getPrice()).total(total).build());
            // Reduce stock
            p.setQuantity(p.getQuantity() - itemReq.getQuantity());
            p.setSoldCount(p.getSoldCount() + itemReq.getQuantity());
            productRepo.save(p);
        }

        BigDecimal deliveryCharge = "DELIVERY".equals(req.getOrderType()) ? new BigDecimal("40") : BigDecimal.ZERO;
        BigDecimal total = subtotal.add(deliveryCharge);
        BigDecimal commission = total.multiply(owner.getCommissionRate()).divide(BigDecimal.valueOf(100));

        Order order = Order.builder()
                .trackingId(trackingIdGen.generate())
                .userId(userId).retailOwnerId(req.getRetailOwnerId())
                .orderType(req.getOrderType()).status("PENDING")
                .subtotal(subtotal).deliveryCharge(deliveryCharge)
                .discount(BigDecimal.ZERO).totalAmount(total)
                .commissionAmount(commission)
                .paymentMethod(req.getPaymentMethod() != null ? req.getPaymentMethod() : "COD")
                .paymentStatus("PENDING")
                .deliveryAddress(req.getDeliveryAddress())
                .deliveryLatitude(req.getDeliveryLatitude() != null ? BigDecimal.valueOf(req.getDeliveryLatitude()) : null)
                .deliveryLongitude(req.getDeliveryLongitude() != null ? BigDecimal.valueOf(req.getDeliveryLongitude()) : null)
                .customerPhone(req.getCustomerPhone()).notes(req.getNotes())
                .items(items).build();

        order = orderRepo.save(order);

        // Notify retailer
        notifService.sendNotification("RETAILER", owner.getId(),
                "New Order Received!", "Order #" + order.getTrackingId() + " - ₹" + total,
                "NEW_ORDER", order.getId());
        // Notify user
        notifService.sendNotification("USER", userId,
                "Order Placed Successfully!", "Your order #" + order.getTrackingId() + " has been placed.",
                "ORDER_PLACED", order.getId());

        return order;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Long retailOwnerId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getRetailOwnerId().equals(retailOwnerId)) throw new RuntimeException("Unauthorized");

        order.setStatus(status);
        if ("DELIVERED".equals(status)) {
            RetailOwner owner = ownerRepo.findById(retailOwnerId).orElseThrow();
            owner.setTotalOrders(owner.getTotalOrders() + 1);
            owner.setTotalRevenue(owner.getTotalRevenue().add(order.getTotalAmount()));
            ownerRepo.save(owner);
            order.setPaymentStatus("COMPLETED");
        }
        order = orderRepo.save(order);

        String userMsg = switch (status) {
            case "ACCEPTED" -> "Your order #" + order.getTrackingId() + " has been accepted!";
            case "REJECTED" -> "Your order #" + order.getTrackingId() + " was rejected.";
            case "READY_TO_PICK" -> "Your order #" + order.getTrackingId() + " is ready for pickup!";
            case "OUT_FOR_DELIVERY" -> "Your order #" + order.getTrackingId() + " is out for delivery!";
            case "DELIVERED" -> "Your order #" + order.getTrackingId() + " has been delivered!";
            default -> "Order #" + order.getTrackingId() + " status updated to: " + status;
        };

        notifService.sendNotification("USER", order.getUserId(), "Order Update", userMsg, "ORDER_UPDATE", order.getId());
        return order;
    }

    public List<Order> getUserOrders(Long userId) { return orderRepo.findByUserIdOrderByCreatedAtDesc(userId); }
    public List<Order> getRetailerOrders(Long retailOwnerId) { return orderRepo.findByRetailOwnerIdOrderByCreatedAtDesc(retailOwnerId); }
    public Order trackOrder(String trackingId) {
        return orderRepo.findByTrackingId(trackingId).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
