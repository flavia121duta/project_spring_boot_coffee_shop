package com.api.project.repository;

import com.api.project.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    @Query("SELECT AVG(p.salary) FROM Profile p")
    double getAverageSalary();
}
