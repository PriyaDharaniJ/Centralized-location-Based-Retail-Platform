package com.retailplatform.repository;
import com.retailplatform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findByRetailOwnerIdOrderByCreatedAtDesc(Long retailOwnerId);
    Optional<Order> findByTrackingId(String trackingId);
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal getTotalPlatformRevenue();
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.retailOwnerId = :id AND o.status = 'DELIVERED'")
    BigDecimal getRevenueByRetailer(@Param("id") Long retailOwnerId);
    @Query("SELECT COALESCE(SUM(o.commissionAmount), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal getTotalCommission();
}
