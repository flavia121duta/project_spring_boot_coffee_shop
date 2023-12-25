package com.api.project.service;

import com.api.project.exception.DuplicateProductException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.model.Review;
import com.api.project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product theProduct) {
        Optional<Product> existingProductSameName = productRepository.findByProductName(theProduct.getProductName());
        existingProductSameName.ifPresent(e -> {
            throw new DuplicateProductException(theProduct.getProductName());
        });
        return productRepository.save(theProduct);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(int theId) {
        Optional<Product> theProduct = productRepository.findById(theId);

        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(theId);
        }

        return theProduct;
    }

    // TODO: handle exception for no available type provided
    public List<Product> getProductsByType(ProductType type) {
        return productRepository.findByProductType(type);
    }


    public Product getProductByName(String name) {
        Optional<Product> theProduct = productRepository.findByProductName(name);
        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(name);
        }
        return theProduct.get();
    }

    public double getAveragePriceForProductType(ProductType type) {
        return productRepository.getAveragePriceForProductType(type);
    }

    public List<Review> findProductAndReviewsByProductId(int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if(theProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }

        return theProduct.get().getReviews();
    }

    // include the id in the @RequestBody of the newProduct
    public Product update(Product newProduct, int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);

        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }

        Product dbProduct = theProduct.get();
        dbProduct.setPrice(newProduct.getPrice());
        dbProduct.setProductName(newProduct.getProductName());
        dbProduct.setProductType(newProduct.getProductType());

        return productRepository.save(dbProduct);
    }

    @Transactional
    public void updatePriceDuringChristmasHoliday(double discount, int theId) {
        Optional<Product> theProduct = productRepository.findById(theId);

        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(theId);
        }

        productRepository.modifyPriceDuringChristmasHoliday(discount, theId);
    }


    public void deleteById(int theId) {
        Optional<Product> theProduct = productRepository.findById(theId);

        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(theId);
        }

        productRepository.deleteById(theId);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }


}
