package com.api.project.repository;

import com.api.project.model.Product;
import com.api.project.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductType(ProductType productType);

    List<Product> findByProductName(String productName);

    @Query("SELECT ROUND(AVG(p.price), 2) FROM Product p WHERE p.productType = :type")
    double getAveragePriceForProductType(ProductType type);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE Product p SET p.price = ROUND(p.price * (1 - :discount/100), 2)  WHERE p.product_id = :id " +
                    "AND CURRENT_DATE BETWEEN '2023-12-20' AND '2023-12-30'")
    void modifyPriceDuringChristmasHoliday(double discount, int id);

    void findProductAndReviewsByProductId(int productId);
}
