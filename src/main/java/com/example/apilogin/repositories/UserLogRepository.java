package com.example.apilogin.repositories;

import com.example.apilogin.entities.UserLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends CrudRepository<UserLogEntity, Integer> {
}
