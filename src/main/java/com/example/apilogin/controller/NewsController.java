package com.example.apilogin.controller;

import com.example.apilogin.service.NewsRepository;
import com.example.apilogin.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;
}
