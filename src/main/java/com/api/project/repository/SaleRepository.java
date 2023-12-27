package com.api.project.repository;

import com.api.project.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT s.saleId FROM Sale s WHERE s.employee.employeeId = :employeeId")
    List<Integer> findSalesTakenByEmployeeGivenByEmployeeId(int employeeId);
}
