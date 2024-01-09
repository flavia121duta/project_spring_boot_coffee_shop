package com.api.project.service;

import com.api.project.dto.SaleRequest;
import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.exception.SaleNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.PaymentMethod;
import com.api.project.model.Product;
import com.api.project.model.Sale;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProductRepository;
import com.api.project.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {
    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void givenSaleObject_createSale_returnSavedSale() {
        // arrange
        SaleRequest saleRequest = new SaleRequest();

        Employee employee = new Employee();
        employee.setEmployeeId(1);

        saleRequest.setEmployeeId(1);
        saleRequest.setProductIds(List.of(1, 2, 2, 3));
        saleRequest.setPaymentMethod(PaymentMethod.CARD);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Product product1 = new Product(); product1.setProductId(1);
        Product product2 = new Product(); product2.setProductId(2);
        Product product3 = new Product(); product3.setProductId(3);

        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        when(productRepository.findById(3)).thenReturn(Optional.of(product3));

        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Sale result = saleService.createSale(saleRequest);

        // assert
        assertNotNull(result);
        assertEquals(saleRequest.getEmployeeId(), result.getEmployee().getEmployeeId());
        assertEquals(saleRequest.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(product1.getProductId(), result.getProducts().get(0).getProductId());

        verify(employeeRepository, times(1)).findById(eq(1));
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    public void givenUnavailableEmployeeId_throwsEmployeeNotFoundException() {
        // arrange
        int employeeId = 1;
        List<Integer> productIds = List.of(101, 102, 103);
        SaleRequest saleRequest = new SaleRequest();
        saleRequest.setEmployeeId(2);
        saleRequest.setProductIds(productIds);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // act
        PotentialStubbingProblem result = assertThrows(
                PotentialStubbingProblem.class,
                () -> saleService.createSale(saleRequest)
        );

        // assert
        assertNotNull(result);
        verify(productRepository, never()).findById(any());
        verify(saleRepository, never()).save(any());
    }

    @Test
    void givenSaleRequestWithUnavailableProductId_createSale_throwsProductNotFoundException() {
        // arrange
        int employeeId = 1;
        List<Integer> nonExistingProductIds = List.of(777, 888, 999);
        SaleRequest saleRequest = new SaleRequest();

        saleRequest.setEmployeeId(employeeId);
        saleRequest.setProductIds(nonExistingProductIds);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));

        for (Integer productId : nonExistingProductIds) {
            lenient().when(productRepository.findById(productId)).thenReturn(Optional.empty());
        }

        // act
        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> saleService.createSale(saleRequest));

        // assert
        assertNotNull(result);
        assertEquals(result.getMessage(), "The product with the id 777 was not found in the database");
        verify(saleRepository, never()).save(any());
    }

    @Test
    public void givenAvailableProductId_getSalesThatContainProductByProductId_returnListOfSales() {
        // arrange
        int productId = 1;
        Product wantedProduct = new Product();
        Product ordinaryproduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(wantedProduct));


        Sale sale1 = new Sale(); sale1.setProducts(List.of(wantedProduct, ordinaryproduct));
        Sale sale2 = new Sale(); sale2.setProducts(List.of(wantedProduct));
        Sale sale3 = new Sale(); sale3.setProducts(List.of(ordinaryproduct));

        when(saleRepository.findAll()).thenReturn(List.of(sale1, sale2, sale3));

        // act
        List<Integer> resultSalesIds = saleService.getSalesThatContainProductByProductId(productId);

        // assert
        assertNotNull(resultSalesIds);
        assertEquals(2, resultSalesIds.size());
        assertTrue(resultSalesIds.contains(sale1.getSaleId()));
        assertTrue(resultSalesIds.contains(sale2.getSaleId()));

        verify(productRepository, times(1)).findById(productId);
        verify(saleRepository, times(1)).findAll();
    }

    @Test
    public void givenUnavailableProductId_getSalesThatContainProductByProductId_throwProductNotFoundException() {
        // arrange
        int productId = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // act
        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> saleService.getSalesThatContainProductByProductId(productId)
        );

        // assert
        assertEquals(result.getMessage(), "The product with the id " + productId + " was not found in the database", "The two error messages should be equal");
        verify(saleRepository, never()).findAll();
    }

    @Test
    public void givenAvailableSaleId_getTotalPriceOfSaleBySaleId_returnsDoubleValue() {
        // arrange
        int saleId = 1;
        Sale theSale = new Sale();
        theSale.setSaleId(saleId);
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(theSale));

        Product product1 = new Product(); product1.setPrice(20.5);
        Product product2 = new Product(); product2.setPrice(10.2);

        theSale.setProducts(List.of(product1, product2));

        double expectedTotalPrice = 30.7;

        // act
        double result = saleService.getTotalPriceOfSaleBySaleId(saleId);

        // assert
        assertEquals(expectedTotalPrice, result, "The two values should be equal");
        verify(saleRepository, times(1)).findById(saleId);
    }

    @Test
    public void givenUnavailableSaleId_getTotalPriceOfSaleBySaleId_throwsSaleNotFound() {
        // arrange
        int saleId = 2024;
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // act
        SaleNotFoundException result = assertThrows(
                SaleNotFoundException.class,
                () -> saleService.getTotalPriceOfSaleBySaleId(saleId)
        );

        // assert
        assertEquals(result.getMessage(), "The sale with the id " + saleId + " was not found in the database" , "The error message should be equal");
        verify(saleRepository, times(1)).findById(saleId);
    }

    @Test
    public void givenAvailableEmployeeId_findSalesTakenByEmployeeGivenByEmployeeId_returnsListOfSaleIds() {
        // arrange
        int employeeId = 1;
        Employee theEmployee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(theEmployee));

        Sale sale1 = new Sale(); sale1.setSaleId(11);
        Sale sale2 = new Sale(); sale2.setSaleId(22);
        theEmployee.setSales(List.of(sale1, sale2));

        List<Integer> expectedList = List.of(11, 22);

        when(saleRepository.findSalesTakenByEmployeeGivenByEmployeeId(employeeId)).thenReturn(expectedList);

        // act
        List<Integer> resultListOfSaleIdsTakenByEmployee = saleService.findSalesTakenByEmployeeGivenByEmployeeId(employeeId);

        // assert
        assertNotNull(resultListOfSaleIdsTakenByEmployee);
        assertEquals(expectedList, resultListOfSaleIdsTakenByEmployee, "The two lists should be equal");
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void givenUnavailableEmployeeId_findSalesTakenByEmployeeGivenByEmployeeId_throwsSaleNotFoundException() {
        // arrange
        int employeeId = 1;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // act
        EmployeeNotFoundException result = assertThrows(
                EmployeeNotFoundException.class,
                () -> saleService.findSalesTakenByEmployeeGivenByEmployeeId(employeeId)
        );

        // assert
        assertEquals(result.getMessage(), "The employee with the id " + employeeId + " was not found in the database", "Should be equal");
        verify(saleRepository, never()).findSalesTakenByEmployeeGivenByEmployeeId(employeeId);
    }

    @Test
    public void givenSaleObjectAndSaleId_updateSale_returnsUpdatedSale() {
        // arrange
        int saleId = 1;

        Sale existingSale = new Sale();
        existingSale.setSaleId(saleId);
        existingSale.setPaymentMethod(PaymentMethod.CASH);

        Sale newSale = new Sale();
        newSale.setPaymentMethod(PaymentMethod.CARD);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(existingSale));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Sale updatedSale = saleService.update(newSale, saleId);

        // assert
        assertNotNull(updatedSale);
        assertEquals(saleId, updatedSale.getSaleId());
        assertEquals(newSale.getPaymentMethod(), updatedSale.getPaymentMethod());

        verify(saleRepository, times(1)).findById(saleId);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    public void givenSaleObjectAndUnavailableEmployeeId_updateSale_throwsSaleNotFoundException() {
        // arrange
        int saleId = 202;
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // act
        SaleNotFoundException result = assertThrows(
                SaleNotFoundException.class,
                () -> saleService.update(new Sale(), saleId)
        );

        // assert
        assertEquals(result.getMessage(), "The sale with the id " + saleId + " was not found in the database", "The two error messages should match");

        verify(saleRepository, times(1)).findById(saleId);
        verify(saleRepository, never()).save(any(Sale.class));
    }
}