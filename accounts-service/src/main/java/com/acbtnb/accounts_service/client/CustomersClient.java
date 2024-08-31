package com.acbtnb.accounts_service.client;

import com.acbtnb.accounts_service.responses.ResponseObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface CustomersClient {
    @GetExchange("/customers/get-customer/{customerID}")
    public ResponseObject getCustomer(@PathVariable String customerID);
}
