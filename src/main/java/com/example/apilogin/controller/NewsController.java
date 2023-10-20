package com.example.apilogin.controller;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.service.NewsRepository;
import com.example.apilogin.service.PagingNewsRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/news")

public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PagingNewsRepository pagingNewsRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<NewsEntity> getAllNews() {
        log.info("GET /news/all");
        // This returns a JSON or XML with the users
        return newsRepository.findAll();
    }

    @GetMapping(path = "/")
    public @ResponseBody Optional<NewsEntity> getNewsItem(@RequestParam Integer id) {
        log.info("GET /news/:id");
        // This returns a JSON or XML with the users
        return newsRepository.findById(id);
    }

    @GetMapping(path = "/paging")
    public @ResponseBody ResponseEntity<Map<String, Object>> getNewsItem(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam Integer pageSize) {
        log.info("GET /news/paging");
        List<NewsEntity> newsItems;
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("localDate").descending());
        Page<NewsEntity> pageNews;
        pageNews = pagingNewsRepository.findAll(paging);
        newsItems = pageNews.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("newsItems", newsItems);
        response.put("currentPage", pageNews.getNumber());
        response.put("totalItems", pageNews.getTotalElements());
        response.put("totalPages", pageNews.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
