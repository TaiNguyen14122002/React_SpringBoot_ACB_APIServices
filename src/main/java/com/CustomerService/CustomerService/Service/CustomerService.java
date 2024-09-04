package com.CustomerService.CustomerService.Service;

import com.CustomerService.CustomerService.Model.Converter.Converter;
import com.CustomerService.CustomerService.Model.Dto.request.CustomerCreateRequest;
import com.CustomerService.CustomerService.Model.Dto.request.CustomerUpdateRequest;
import com.CustomerService.CustomerService.Model.Entity.Customers;
import com.CustomerService.CustomerService.Model.Mapper.MapperCustomer;
import com.CustomerService.CustomerService.Repository.CustomerRepository;
import com.CustomerService.CustomerService.Response.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MapperCustomer mapperCustomer;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Validator validator;

    private String server = "http://localhost:8080";

    public Customers create(CustomerCreateRequest request){

        if(customerRepository.existsByCustomerId(request.getCustomerId()))
            throw new RuntimeException("Customer exist!");

        Customers customer = mapperCustomer.toCreateCustomer(request);

        if (customer.getCustomer_business()){
            customer.setSegment(null);
        }

        customer.setCreatedAt(java.time.LocalDateTime.now());
        customer.setDeleted(false);
        return customerRepository.save(customer);
    }

    public List<Customers> getCustomers(){
        return customerRepository.findAll();
    }

    public Customers getCustomer(String customerID){
        return customerRepository.findById(customerID).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public  Customers updateCustomer(String customerID, CustomerUpdateRequest request){
        Customers customer = getCustomer(customerID);
        mapperCustomer.updateCustomer(customer, request);

        if (customer.getCustomer_business()){
            customer.setSegment(null);
        }
        return customerRepository.save(customer);
    }

    public Customers deleteCustomer(String customerID){

        Customers customer = getCustomer(customerID);
        customer.setDeleted(true);
        customer.setDeletedAt(java.time.LocalDateTime.now());

        return customerRepository.save(customer);
    }

    public APIResponse createListCustomers(List<CustomerCreateRequest> list){
        List<CustomerCreateRequest> validCustomer = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        for (CustomerCreateRequest customer : list) {
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(customer);
            if (violations.isEmpty()) {
                validCustomer.add(customer);
            } else {
                messages.add("IDNo " + customer.getCustomerId() + " have some errors, please check information");
            }
        }

        List<Customers> listCustomers = convertToCustomerList(validCustomer);
        List<Customers> result = customerRepository.saveAll(listCustomers);

        APIResponse apiResponse = new APIResponse();

        if (messages.isEmpty()) {
            apiResponse.setStatus("200");
            apiResponse.setData(result);
        } else {
            apiResponse.setStatus("207");
            apiResponse.setData(messages);
            apiResponse.setMessage("Some customers invalid, check list customers invalid in data");
        }

        return apiResponse;

    }

    public Customers uploadImage(String customerID, MultipartFile front_image, MultipartFile back_image){
        Customers customer = getCustomer(customerID);

        try {
            String url_front = saveFile(front_image, customerID + "_front_image.");
            customer.setFrontImage(server + url_front);

            String url_back = saveFile(back_image, customerID + "_back_image.");
            customer.setBackImage(server + url_back);

        } catch (IOException e) {
            throw new RuntimeException("Error when upload image!");
        }

        return customerRepository.save(customer);
    }

    public String saveFile(MultipartFile file, String customerID) throws IOException {

        String fileExtension;
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            fileExtension = originalFilename;
        }

        Path path = Paths.get(uploadDir + File.separator + customerID + fileExtension);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return "/uploads/" + customerID + fileExtension;
    }

    public CustomerService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public List<Customers> convertToCustomerList(List<CustomerCreateRequest> requests) {
        return requests.stream().map(Converter::convertToCustomer).collect(Collectors.toList());
    }
}
