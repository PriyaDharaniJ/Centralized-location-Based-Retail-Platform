package com.retailplatform.repository;
import com.retailplatform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRetailOwnerId(Long retailOwnerId);
    List<Product> findByRetailOwnerIdAndIsAvailableTrue(Long retailOwnerId);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) AND p.isAvailable = true")
    List<Product> searchByName(@Param("query") String query);
    List<Product> findByCategoryIdAndIsAvailableTrue(Long categoryId);
}
