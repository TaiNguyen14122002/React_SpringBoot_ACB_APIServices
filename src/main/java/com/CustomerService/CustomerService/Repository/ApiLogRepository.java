package com.CustomerService.CustomerService.Repository;

import com.CustomerService.CustomerService.Model.Entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}
