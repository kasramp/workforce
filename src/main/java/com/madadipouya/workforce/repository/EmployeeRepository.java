package com.madadipouya.workforce.repository;

import com.madadipouya.workforce.model.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Employee} to interact with the database
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByUuid(String uuid);

    Optional<Employee> findByEmail(String email);

    @Transactional(readOnly = true)
    @Cacheable("employees")
    List<Employee> findAll();

    @Transactional
    Employee save(Employee employee);

    boolean existsByUuid(String uuid);

    boolean existsByEmail(String email);

    void delete(Employee employee);

    void deleteById(String uuid);
}
