package com.example.apilogin.controller;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.exceptions.NewsException;
import com.example.apilogin.repositories.NewsRepository;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.services.PagingNewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
            PagingNewsService pagingNewsService,
            NewsRepository newsRepository) {
        this.pagingNewsService = pagingNewsService;
        this.newsRepository = newsRepository;
    }

    @GetMapping(path = "/")
    public @ResponseBody Optional<NewsEntity> getNewsItem(@RequestParam Integer id) {
        log.info("GET /news/:id");
        // This returns a JSON or XML with the users
        return newsRepository.findById(id);
    }

    @GetMapping(path = "/paging")
    public @ResponseBody ResponseEntity<Map<String, Object>> getNewsItem(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam Integer pageSize,
            HttpServletRequest httpServletRequest) {
        log.info("GET /news/paging");
        try {
            return buildResponse(
                    pageNumber,
                    pageSize);
        } catch (Exception e) {
            throw NewsException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_REQUEST_NEWS)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(getUserAccount())
                    .build();
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity<Map<String, Object>> deleteNewsItems(
            @RequestBody
            Integer[] deleteArray,
            @RequestParam(defaultValue = "0")
            Integer pageNumber,
            @RequestParam
            Integer pageSize,
            HttpServletRequest httpServletRequest) {
        log.info("POST /news/delete");
        try {
            pagingNewsService.deleteAllById(Arrays.asList(deleteArray));
            return buildResponse(
                    pageNumber,
                    pageSize);
        } catch (Exception e) {
            throw NewsException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_NEWS_DELETE)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(getUserAccount())
                    .build();
        }
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            Integer pageNumber,
            Integer pageSize) {
        List<NewsEntity> newsItems;
        Pageable paging = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("localDate")
                        .descending());
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

    private String getUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();
        UserPrincipal principal = jwtToPrincipalConverter.convert(token);
        return principal.getAccount();
    }
}
