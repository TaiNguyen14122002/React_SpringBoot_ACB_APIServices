package com.CustomerService.CustomerService.Controller;

import com.CustomerService.CustomerService.Response.APIResponse;
import com.CustomerService.CustomerService.Model.Dto.request.CustomerCreateRequest;
import com.CustomerService.CustomerService.Model.Dto.request.CustomerUpdateRequest;
import com.CustomerService.CustomerService.Model.Entity.Customers;
import com.CustomerService.CustomerService.Service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    APIResponse<Customers> createCustomer(@RequestBody @Valid CustomerCreateRequest request){
        APIResponse<Customers> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.create(request));
        return apiResponse;
    }

    @GetMapping()
    APIResponse<List<Customers>> getCustomers(){
        APIResponse<List<Customers>> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.getCustomers());
        return apiResponse;
    }

    @GetMapping("/{customerID}")
    APIResponse<Customers> getCustomer(@PathVariable String customerID){
        APIResponse<Customers> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.getCustomer(customerID));
        return apiResponse;
    }

    @PutMapping("/{customerID}")
    APIResponse<Customers> updateCustomer(@PathVariable String customerID, @RequestBody @Valid CustomerUpdateRequest request){
        APIResponse<Customers> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.updateCustomer(customerID, request));
        return apiResponse;
    }

    @PatchMapping("/{customerID}")
    APIResponse<Customers> deleteCustomer(@PathVariable String customerID){
        APIResponse<Customers> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.deleteCustomer(customerID));
        return apiResponse;
    }

    @PostMapping("/{customerID}/upload-images")
    APIResponse<Customers> uploadImages(
            @RequestParam("image_front") MultipartFile image_front,
            @RequestParam("image_back") MultipartFile image_back,
            @PathVariable String customerID)
    {
        APIResponse<Customers> apiResponse = new APIResponse<>();
        apiResponse.setData(customerService.uploadImage(customerID, image_front, image_back));
        return apiResponse;
    }

    @PostMapping("/create-list")
    public APIResponse createListCustomers(@RequestBody List<CustomerCreateRequest> list) {
        return customerService.createListCustomers(list);
    }

}
