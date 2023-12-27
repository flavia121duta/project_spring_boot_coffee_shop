package com.api.project.repository;

import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductType(ProductType productType);

    Optional<Product> findByProductName(String productName);

    @Query("SELECT ROUND(AVG(p.price), 2) FROM Product p WHERE p.productType = :type")
    double getAveragePriceForProductType(ProductType type);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE Product p SET p.price = ROUND(p.price * (1 - :discount/100), 2)  WHERE p.product_id = :id " +
                    "AND CURRENT_DATE BETWEEN '2023-12-20' AND '2023-12-30'")
    void modifyPriceDuringChristmasHoliday(double discount, int id);

    @Query("SELECT DISTINCT p FROM Sale s INNER JOIN s.products p WHERE CAST(s.timeOfOrder as date) BETWEEN :date1 AND :date2")
    List<Product> getProductOrderedBetweenDates(LocalDate date1, LocalDate date2);

    @Query("SELECT p FROM Product p WHERE NOT EXISTS (SELECT r FROM Review r WHERE r.product.productId = p.productId)")
    List<Product> findProductsWithNoReview();

    @Query("SELECT p, AVG(r.stars) AS averageRating, COUNT(r) AS reviewCount " +
            "FROM Product p LEFT JOIN p.reviews r " +
            "GROUP BY p.productId")
    List<Object[]> getAverageRatingAndReviewCountPerProduct();
}
