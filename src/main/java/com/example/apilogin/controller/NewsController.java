package com.example.apilogin.controller;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.service.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/news")

public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<NewsEntity> getAllNews() {
        // This returns a JSON or XML with the users
        return newsRepository.findAll();
    }
    @GetMapping(path = "/")
    public @ResponseBody Optional<NewsEntity> getNewsItem(@RequestParam Integer id) {
        // This returns a JSON or XML with the users
        return newsRepository.findById(id);
    }
}
