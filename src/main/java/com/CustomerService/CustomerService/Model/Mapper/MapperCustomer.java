package com.CustomerService.CustomerService.Model.Mapper;

import com.CustomerService.CustomerService.Model.Dto.request.CustomerCreateRequest;
import com.CustomerService.CustomerService.Model.Dto.request.CustomerUpdateRequest;
import com.CustomerService.CustomerService.Model.Entity.Customers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MapperCustomer {
    Customers toCreateCustomer(CustomerCreateRequest request);
    void updateCustomer(@MappingTarget Customers customers, CustomerUpdateRequest request);
}
