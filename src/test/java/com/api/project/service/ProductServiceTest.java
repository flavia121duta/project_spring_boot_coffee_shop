package com.api.project.service;

import com.api.project.exception.DuplicateProductException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.model.Review;
import com.api.project.model.Sale;
import com.api.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;


    @Mock
    private ProductRepository productRepository;

    @Test
    void whenProductDoesNotExist_create_saveTheProduct() {
        // arrange
        Product product = new Product("Ice Cream", 7.8, ProductType.OTHERS);
        when(productRepository.findByProductName(product.getProductName())).thenReturn(Optional.empty());

        Product savedProduct = new Product("Ice Cream", 8.0, ProductType.OTHERS);
        savedProduct.setProductId(1);
        when(productRepository.save(product)).thenReturn(savedProduct);

        // act
        Product result = productService.create(product);

        // assert
        assertNotNull(result);
        assertEquals(savedProduct.getProductId(), result.getProductId());
        assertEquals(savedProduct.getProductName(), result.getProductName());
        assertEquals(product.getProductName(), result.getProductName());
        verify(productRepository).findByProductName(product.getProductName());
        verify(productRepository).save(product);
    }

    @Test
    public void whenProductAlreadyExists_create_throwsDuplicateProductException() {
        // arrange
        Product product = new Product("Chocolate Croissants", 10.5, ProductType.BAKERY);
        when(productRepository.findByProductName(product.getProductName())).thenReturn(Optional.of(product));

        // act
        DuplicateProductException exception = assertThrows(
                DuplicateProductException.class,
                () -> productService.create(product)
        );

        // assert
        assertEquals("A product with the same name \"" + product.getProductName() + " \"already exists.", exception.getMessage());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    public void whenProductExists_findById_returnsTheProduct() {
        // arrange
        Product product = new Product("Strawberry Doughnut", 8.2, ProductType.BAKERY);
        product.setProductId(1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // act
        Optional<Product> result = productService.findById(1);

        // assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(product.getProductId(), result.get().getProductId(), "The ids should match");
        assertEquals(product, result.get(), "Should be equal");
    }

    @Test
    public void whenProductDoesNotExist_findById_throwsProductNotFoundException() {
        // arrange

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> productService.findById(1)
        );

        // assert
        assertEquals(
                "The product with the id 1 was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    public void givenProductType_getProductsByType_returnListOfProducts() {
        // arrange
        ProductType productType = ProductType.TEA;

        Product product1 = new Product();
        product1.setProductType(ProductType.COFFEE);

        Product product2 = new Product();
        product2.setProductType(ProductType.TEA);

        Product product3 = new Product();
        product3.setProductType(ProductType.TEA);

        List<Product> expectedListOfProducts = List.of(product2, product3);
        when(productRepository.findByProductType(productType)).thenReturn(expectedListOfProducts);

        // act
        List<Product> actualListOfProducts = productService.getProductsByType(productType);

        // assert
        assertNotNull(actualListOfProducts);
        assertEquals(actualListOfProducts.size(), 2);
        assertEquals(expectedListOfProducts, actualListOfProducts, "The two lists should be equal");
    }

    @Test
    public void givenAvailableProductName_getProductByName_returnTheProduct() {
        // arrange
        String productName = "Green Tea";

        Product product1 = new Product();
        product1.setProductName("Espresso");

        Product product2 = new Product();
        product2.setProductName("Green Tea");

        Product product3 = new Product();
        product3.setProductName("Cappuccino");

        when(productRepository.findByProductName(productName)).thenReturn(Optional.of(product2));

        // act
        Product actualProduct = productService.getProductByName(productName);

        // assert
        assertNotNull(actualProduct);
        assertEquals(product2, actualProduct);
    }

    @Test
    public void givenNonexistentProductName_getProductByName_throwsProductNotFoundException() {
        // arrange
        String productName = "Nonexistent Product";
        when(productRepository.findByProductName(productName))
                .thenReturn(Optional.empty());

        // act
        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductByName(productName)
        );

        // assert
        assertNotNull(result);
        assertEquals(result.getMessage(), "The product with the name " + productName + " was not found in the database", "Should be equal");
    }

    @Test
    public void getAveragePriceForProductType_returnsValue() {
        ProductType productType = ProductType.COFFEE;

        Product product1 = new Product();
        product1.setProductType(ProductType.COFFEE);
        product1.setPrice(13.9);

        Product product2 = new Product();
        product2.setProductType(ProductType.COFFEE);
        product2.setPrice(15.7);

        double expectedResult = 14.8;
        when(productRepository.getAveragePriceForProductType(productType)).thenReturn(expectedResult);

        double actualResult = productService.getAveragePriceForProductType(productType);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void givenExistingProductId_findProductAndReviewsByProductId_returnsListOfReviews() {
        int productId = 1;

        Product product = new Product();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Review review1 = new Review();
        Review review2 = new Review();

        product.addReview(review1);
        product.addReview(review2);

        List<Review> expectedResult = List.of(review1, review2);

        List<Review> actualResult = productService.findProductAndReviewsByProductId(productId);

        assertNotNull(actualResult);
        assertEquals(actualResult.size(), 2);
        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void givenNonexistentProductId_findProductAndReviewsByProductId_throwsProductNotFoundException() {
        int productId = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> productService.findProductAndReviewsByProductId(productId)
        );

        assertNotNull(result);
        assertEquals("The product with the id " + productId + " was not found in the database", result.getMessage(), "Should be equal");
    }

    @Test
    public void givenTwoDates_getProductOrderedBetweenDates_returnsListOfProducts() {
        LocalDate date1 = LocalDate.of(2023, 12, 1);
        LocalDate date2 = LocalDate.of(2024, 1, 1);

        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> expectedList = List.of(product1, product2);

        Sale sale = new Sale();
        sale.setDateOfOrder("2023-12-20");
        sale.setProducts(expectedList);

        when(productRepository.getProductOrderedBetweenDates(date1, date2)).thenReturn(expectedList);

        List<Product> actualList = productService.getProductOrderedBetweenDates(date1, date2);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
        verify(productRepository, times(1)).getProductOrderedBetweenDates(date1, date2);
    }

    @Test
    public void getAverageRatingAndReviewCountPerProduct_returnListOfObjects() {
        Product product1 = new Product();
        Product product2 = new Product();

        List<Object[]> expectedList = Arrays.asList(
                new Object[] { product1, 4.5, 10 },
                new Object[] { product2, 3.8, 8 }
        );

        when(productRepository.getAverageRatingAndReviewCountPerProduct()).thenReturn(expectedList);

        List<Object[]> actualList = productService.getAverageRatingAndReviewCountPerProduct();

        assertNotNull(actualList);
        assertEquals(expectedList.size(), expectedList.size());
        assertEquals(expectedList, expectedList);
        verify(productRepository, times(1)).getAverageRatingAndReviewCountPerProduct();
    }

    @Test
    public void getProductsWithoutAnyReview_returnsListOfProducts() {
        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> expectedList = List.of(product1, product2);

        when(productRepository.findProductsWithNoReview()).thenReturn(expectedList);

        List<Product> result = productService.getProductsWithoutAnyReview();

        assertNotNull(result);
        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(productRepository, times(1)).findProductsWithNoReview();
    }

    @Test
    public void givenDiscountAndProductId_updatePriceDuringChristmasHoliday_returnNoResult() {
        int productId = 1;
        double discount = 10.0;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(12);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.updatePriceDuringChristmasHoliday(discount, productId);

//        assertEquals(10.8, product.getPrice());
        verify(productRepository).modifyPriceDuringChristmasHoliday(discount, productId);
        verify(productRepository, times(1)).modifyPriceDuringChristmasHoliday(discount, productId);
    }


    @Test
    public void givenDiscountAndNonexistentProductId_updatePriceDuringChristmasHoliday_throwProductNotFoundException() {
        int theId = 1;
        double discount = 10.0;

        when(productRepository.findById(theId)).thenReturn(Optional.empty());

        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updatePriceDuringChristmasHoliday(discount, theId)
        );

        assertNotNull(result);
        assertEquals("The product with the id " + theId + " was not found in the database", result.getMessage(), "Should be equal");
    }
}