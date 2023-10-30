package com.example.apilogin.service;

import com.example.apilogin.entities.NewsEntity;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<NewsEntity, Integer> {
    NewsEntity findByTitle(String title);
}
