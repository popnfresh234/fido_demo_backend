package com.example.apilogin.controller;

import com.example.apilogin.model.response.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(path = "/uaf")
public class UafController {
    @GetMapping(path="/test")
    public Response test (){
        return new Response("This is a test");
    }
}
