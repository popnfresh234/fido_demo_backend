package com.example.apilogin.service;

import com.example.apilogin.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingNewsRepository extends JpaRepository<NewsEntity, Integer> {
}
