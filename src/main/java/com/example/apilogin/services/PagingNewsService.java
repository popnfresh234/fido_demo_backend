package com.example.apilogin.services;

import com.example.apilogin.entities.NewsEntity;
import com.example.apilogin.repositories.PagingNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagingNewsService {
    @Autowired
    private PagingNewsRepository pagingNewsRepository;

    public Page<NewsEntity> findAll(Pageable paging){
        return pagingNewsRepository.findAll(paging);
    }

    public void deleteAllById(List<Integer> deleteArray){
        pagingNewsRepository.deleteAllById(deleteArray);
    }
}
