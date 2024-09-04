package com.CustomerService.CustomerService.Repository;

import com.CustomerService.CustomerService.Model.Entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, String> {
    boolean existsByCustomerId(String customerID);
}
