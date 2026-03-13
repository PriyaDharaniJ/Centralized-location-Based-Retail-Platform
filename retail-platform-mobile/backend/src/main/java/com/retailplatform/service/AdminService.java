package com.retailplatform.service;
import com.retailplatform.entity.RetailOwner;
import com.retailplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired private RetailOwnerRepository ownerRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private NotificationService notifService;

    public List<RetailOwner> getPendingRetailers() { return ownerRepo.findByIsApprovedFalse(); }
    public List<RetailOwner> getAllRetailers() { return ownerRepo.findAll(); }

    public RetailOwner approveRetailer(Long id) {
        RetailOwner owner = ownerRepo.findById(id).orElseThrow(() -> new RuntimeException("Retailer not found"));
        owner.setIsApproved(true);
        owner = ownerRepo.save(owner);
        notifService.sendNotification("RETAILER", id,
                "Shop Approved!", "Congratulations! Your shop '" + owner.getShopName() + "' has been approved. You can now add products.",
                "APPROVAL", id);
        return owner;
    }

    public RetailOwner rejectRetailer(Long id) {
        RetailOwner owner = ownerRepo.findById(id).orElseThrow(() -> new RuntimeException("Retailer not found"));
        owner.setIsApproved(false);
        owner.setIsActive(false);
        return ownerRepo.save(owner);
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRetailers", ownerRepo.count());
        stats.put("approvedRetailers", ownerRepo.findByIsApprovedTrue().size());
        stats.put("pendingRetailers", ownerRepo.findByIsApprovedFalse().size());
        stats.put("totalUsers", userRepo.count());
        stats.put("totalOrders", orderRepo.count());
        stats.put("totalRevenue", orderRepo.getTotalPlatformRevenue());
        stats.put("totalCommission", orderRepo.getTotalCommission());

        List<RetailOwner> retailers = ownerRepo.findAll();
        retailers.forEach(r -> {
            BigDecimal rev = orderRepo.getRevenueByRetailer(r.getId());
            r.setTotalRevenue(rev != null ? rev : BigDecimal.ZERO);
        });
        stats.put("retailers", retailers);
        return stats;
    }
}
