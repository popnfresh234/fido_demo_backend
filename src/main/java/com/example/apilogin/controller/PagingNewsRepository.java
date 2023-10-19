package com.example.apilogin.controller;

import com.example.apilogin.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagingNewsRepository extends JpaRepository<NewsEntity, Integer> {
}
