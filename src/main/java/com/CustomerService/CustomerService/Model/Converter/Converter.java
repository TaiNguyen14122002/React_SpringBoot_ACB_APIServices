package com.CustomerService.CustomerService.Model.Converter;

import com.CustomerService.CustomerService.Model.Dto.request.CustomerCreateRequest;
import com.CustomerService.CustomerService.Model.Entity.Customers;
import com.CustomerService.CustomerService.Model.Mapper.MapperCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Converter {

    public static Customers convertToCustomer(CustomerCreateRequest request) {
        if (request == null) {
            return null;
        }

        Customers customer = new Customers();
        customer.setCustomerId(request.getCustomerId());
        customer.setIssuedOn(request.getIssuedOn());
        customer.setIssuedAt(request.getIssuedAt());
        customer.setFrontImage("");
        customer.setBackImage("");
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setNation(request.getNation());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setCustomer_business(request.getCustomer_business());
        if (request.getCustomer_business()){
            customer.setSegment(null);
        } else  customer.setSegment(request.getSegment());

        return customer;
    }
}
