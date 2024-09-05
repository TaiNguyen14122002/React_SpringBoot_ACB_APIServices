package com.CustomerService.CustomerService.Exception;

import com.CustomerService.CustomerService.Response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceotionHandle {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<APIResponse> handlingRuntimeException(RuntimeException exception){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus("501");
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingValidation(MethodArgumentNotValidException exception){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus("400");
        apiResponse.setMessage(exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
