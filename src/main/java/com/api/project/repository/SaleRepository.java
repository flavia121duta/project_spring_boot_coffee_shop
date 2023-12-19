package com.api.project.repository;

import com.api.project.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
