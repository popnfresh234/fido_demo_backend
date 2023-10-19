package com.example.apilogin.service;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<NewsEntity, Integer> {
    NewsEntity findByTitle(String title);
}
