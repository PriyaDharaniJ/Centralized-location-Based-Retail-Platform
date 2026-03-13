package com.retailplatform.repository;
import com.retailplatform.entity.RetailOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
public interface RetailOwnerRepository extends JpaRepository<RetailOwner, Long> {
    Optional<RetailOwner> findByEmail(String email);
    boolean existsByEmail(String email);
    List<RetailOwner> findByIsApprovedTrue();
    List<RetailOwner> findByIsApprovedFalse();
    @Query("SELECT r FROM RetailOwner r WHERE r.isApproved = true AND r.isActive = true")
    List<RetailOwner> findAllApprovedActive();
}
