package com.api.project.controller;

import com.api.project.dto.SaleRequest;
import com.api.project.mapper.SaleMapper;
import com.api.project.model.Sale;
import com.api.project.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleRestController {
    private final SaleService saleService;
    private final SaleMapper saleMapper;

    @Autowired
    public SaleRestController(SaleService saleService, SaleMapper saleMapper) {
        this.saleService = saleService;
        this.saleMapper = saleMapper;
    }

    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody @Valid SaleRequest saleRequest) {
        return ResponseEntity
                .ok()
                .body(saleService.createSale(saleRequest));
    }

    @GetMapping
    public List<Sale> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<?> findById(@PathVariable int saleId) {
        return ResponseEntity.ok().body(saleService.findById(saleId));
    }

    @GetMapping("/{saleId}/total")
    public double getTotalPrice(@PathVariable int saleId) {
        return saleService.getTotalPriceOfSaleBySaleId(saleId);
    }

    @GetMapping("/product/{productId}")
    public List<Integer> getAllSalesByProductId(@PathVariable int productId) {
        return saleService.getSalesThatContainProductByProductId(productId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<Integer> findSalesTakenByEmployeeGivenByEmployeeId(@PathVariable int employeeId) {
        return saleService.findSalesTakenByEmployeeGivenByEmployeeId(employeeId);
    }

//    @GetMapping("/most-wanted")
//    public ResponseEntity<?> getBestSellingProduct() {
//        return ResponseEntity.ok().body()saleService.getBestSellingProduct();
//    }

    @PutMapping("/{saleId}")
    public ResponseEntity<Sale> updateSale(@RequestBody @Valid SaleRequest theSale,
                                                   @PathVariable int saleId) {
        return ResponseEntity.ok().body(
                saleService.update(saleMapper.saleRequestToOrder(theSale), saleId)
        );
    }

    @DeleteMapping("/{saleId}")
    public String deleteSale(@PathVariable int saleId) {
        saleService.deleteById(saleId);
        return "The sale with the id: " + saleId + " was successfully deleted.";
    }

    @DeleteMapping
    public String deleteAllSles() {
        int numberOfSles = saleService.findAll().size();
        saleService.deleteAll();
        return "All " + numberOfSles + " sales were successfully deleted from the database.";
    }
}
