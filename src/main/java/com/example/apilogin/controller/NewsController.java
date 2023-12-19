package com.example.apilogin.controller;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.exceptions.NewsException;
import com.example.apilogin.repositories.NewsRepository;
import com.example.apilogin.services.PagingNewsService;
import com.example.apilogin.utils.AuthUtils;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
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
    private static final String OPERATION_NEWS_DELETE = "delete news";
    private static final String OPERATION_REQUEST_NEWS = "request news";

    private final PagingNewsService pagingNewsService;
    private final NewsRepository newsRepository;

    public NewsController(
            PagingNewsService pagingNewsService, NewsRepository newsRepository) {
        this.pagingNewsService = pagingNewsService;
        this.newsRepository = newsRepository;
    }

    @GetMapping(path="/all")
    public Iterable<NewsEntity> getNews(){
        return newsRepository.findAll();
    }

    @GetMapping(path = "/")
    public @ResponseBody Optional<NewsEntity> getNewsItem(@RequestParam Integer id) {
        log.info(LogUtils.buildRouteLog("GET /news/:id"));
        return newsRepository.findById(id);
    }

    @GetMapping(path = "/paging")
    public @ResponseBody ResponseEntity<Map<String, Object>> getNewsItem(
            @RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam Integer pageSize,
            HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("GET /news/paging"));
        try {
            return buildResponse(
                    pageNumber,
                    pageSize);
        } catch (Exception e) {
            throw NewsException.builder().msg(e.getMessage()).operation(OPERATION_REQUEST_NEWS)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity<Map<String, Object>> deleteNewsItems(
            @RequestBody Integer[] deleteArray, @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam Integer pageSize, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /news/delete"));
        try {
            pagingNewsService.deleteAllById(Arrays.asList(deleteArray));
            return buildResponse(
                    pageNumber,
                    pageSize);
        } catch (Exception e) {
            throw NewsException.builder().msg(e.getMessage()).operation(OPERATION_NEWS_DELETE)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            Integer pageNumber, Integer pageSize) {
        List<NewsEntity> newsItems;
        Pageable paging = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("localDate").descending());
        Page<NewsEntity> pageNews;
        pageNews = pagingNewsService.findAll(paging);
        newsItems = pageNews.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put(
                "newsItems",
                newsItems);
        response.put(
                "currentPage",
                pageNews.getNumber());
        response.put(
                "totalItems",
                pageNews.getTotalElements());
        response.put(
                "totalPages",
                pageNews.getTotalPages());
        return new ResponseEntity<>(
                response,
                HttpStatus.OK);

    }
}
