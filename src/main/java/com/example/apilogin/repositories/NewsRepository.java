package com.example.apilogin.repositories;

import com.example.apilogin.entities.NewsEntity;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<NewsEntity, Integer> {
}