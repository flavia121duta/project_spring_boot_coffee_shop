package com.api.project.repository;

import com.api.project.model.Employee;
import com.api.project.model.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByLastName(String theLastName);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE e FROM Employee e INNER JOIN Profile p ON e.profile_id = p.profile_id WHERE p.shift_type = 'SEASONAL'" +
                    " AND DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(), p.date_of_birth)), '%Y') < 21")
    void deleteSeasonalEmployees();

    @Query("SELECT e FROM Employee e INNER JOIN Profile p ON p.profileId = e.profile.profileId WHERE p.salary >= :theSalary")
    List<Employee> findEmployeesAboveSalary(double theSalary);

    @Query("SELECT e FROM Employee e INNER JOIN Profile p ON p.profileId = e.profile.profileId WHERE p.shiftType = :shiftType")
    List<Employee> findEmployeesByShiftType(ShiftType shiftType);

    @Query("SELECT e FROM Employee e WHERE NOT EXISTS (SELECT s FROM Sale s WHERE s.employee.employeeId = e.employeeId)")
    List<Employee> findEmployeesWithNoSalesTaken();
}
