package com.example.apilogin.repositories;

import com.example.apilogin.entities.NewsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends CrudRepository<NewsEntity, Integer> {
}