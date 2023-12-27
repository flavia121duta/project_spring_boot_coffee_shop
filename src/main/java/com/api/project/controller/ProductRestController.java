package com.api.project.controller;

import com.api.project.dto.ProductRequest;
import com.api.project.mapper.ProductMapper;
import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.model.Review;
import com.api.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductRestController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll(@RequestParam(required = false) ProductType type) {
        if (type == null) {
            return ResponseEntity.ok().body(productService.findAll());
        }

        return ResponseEntity.ok().body(productService.getProductsByType(type));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable int productId) {
        return ResponseEntity.ok().body(productService.findById(productId));
    }

    @GetMapping("/product-name/{productName}")
    public ResponseEntity<?> getProductsByName(@PathVariable String productName) {
        return ResponseEntity.ok().body(productService.getProductByName(productName));
    }

    @GetMapping("/average-price/{type}")
    public double getAveragePriceOfProductType(@PathVariable ProductType type) {
        return productService.getAveragePriceForProductType(type);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<Review>> findProductAndReviewByProductId(@PathVariable int productId) {
        return ResponseEntity.ok().body(productService.findProductAndReviewsByProductId(productId));
    }

    @GetMapping("/sales/sold")
    public ResponseEntity<List<Product>> getProductOrderedBetweenDates(@RequestParam LocalDate dateStart, @RequestParam LocalDate dateEnd) {
        return ResponseEntity.ok().body(productService.getProductOrderedBetweenDates(dateStart, dateEnd));
    }

    @GetMapping("/no-reviews")
    public ResponseEntity<List<Product>> getProductsWithoutAnyReview() {
        return ResponseEntity.ok().body(productService.getProductsWithoutAnyReview());
    }

    @GetMapping("/average-rating/number-of-reviews")
    public List<Object[]> getAverageRatingAndReviewCountPerProduct() {
        return productService.getAverageRatingAndReviewCountPerProduct();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid ProductRequest theProduct) {
        return ResponseEntity
                .ok()
                .body(productService.create(productMapper.productRequestToProduct(theProduct)));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@RequestBody @Valid ProductRequest theProduct,
                                                 @PathVariable int productId) {
        return ResponseEntity.ok().body(
                productService.update(
                        productMapper.productRequestToProduct(theProduct),
                        productId
                ));
    }

    @PutMapping("/discount/{productId}")
    public void addDiscountToProduct(@RequestParam(required = false) Double discount, @PathVariable int productId) {
        if (discount == null) {
            discount = 15.0;
        }

        productService.updatePriceDuringChristmasHoliday(discount, productId);
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        productService.deleteById(productId);
        return "The product with the id " + productId + " was successfully deleted.";
    }

    @DeleteMapping
    public String deleteAllProducts() {
        int numberOfProducts = productService.findAll().size();
        productService.deleteAll();
        return "All " + numberOfProducts + " products were successfully deleted from the database.";
    }
}
