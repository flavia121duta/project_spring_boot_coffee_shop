package com.api.project.controller;

import com.api.project.dto.EmployeeRequest;
import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.mapper.EmployeeMapper;
import com.api.project.model.Employee;
import com.api.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeRestController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody @Valid EmployeeRequest theEmployee, @RequestParam(required = false) Integer profileId) {
        if (profileId == null) {
            return ResponseEntity
                    .ok()
                    .body(employeeService.create(
                            employeeMapper.convertRequestToEmployee(theEmployee)
                    ));
        }

        return ResponseEntity
                .ok()
                .body(employeeService.create(
                        employeeMapper.convertRequestToEmployee(theEmployee),
                        profileId
                ));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int employeeId) {
        return ResponseEntity.ok().body(employeeService.findById(employeeId));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> findAll(@RequestParam(required = false) String lastName) {
        if (lastName == null) {
            return ResponseEntity.ok().body(
                    employeeService.findAll()
            );
        }

        return ResponseEntity.ok().body(
                employeeService.findByLastName(lastName)
        );
    }

    @GetMapping("/name/{lastName}")
    public ResponseEntity<?> findByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok().body(employeeService.findByLastName(lastName));
    }


    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid EmployeeRequest theEmployee,
                                                   @PathVariable int employeeId,
                                                   @RequestParam(required = false) Integer profileId) {

        if (profileId == null) {
            return ResponseEntity.ok().body(
                    employeeService.update(
                            employeeMapper.convertRequestToEmployee(theEmployee),
                            employeeId
                    ));
        }

        return ResponseEntity.ok().body(
                employeeService.update(
                        employeeMapper.convertRequestToEmployee(theEmployee),
                        employeeId,
                        profileId
                ));

    }

    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteById(employeeId);
        return "The employee with the id: " + employeeId + " was successfully deleted.";
    }

    @DeleteMapping("/seasonal")
    public void deleteSeasonalEmployees() {
        employeeService.deleteByShiftTypeSeasonal();
    }

    @DeleteMapping
    public String deleteAllEmployees() {
        int numEmployees = employeeService.findAll().size();
        employeeService.deleteAll();
        return "All " + numEmployees + " employees were successfully deleted from the database.";
    }
}
