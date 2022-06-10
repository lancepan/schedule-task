package com.example.scheduletask.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = RuntimeException.class)
    public ResponseBody handleException(Exception e, HttpServletResponse response){
        ResponseBody responseBody = new ResponseBody();
        responseBody.setCode(response.getStatus());
        responseBody.setMessage(e.getMessage());
        return responseBody;
    }
}
@Data
class ResponseBody{
    private int code;
    private String message;
}