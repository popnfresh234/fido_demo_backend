package com.example.apilogin.service;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserLogRepository extends CrudRepository<UserLogEntity, Integer> {
}
