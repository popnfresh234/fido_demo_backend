package com.example.apilogin.service;
import com.example.apilogin.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<UserEntity, Integer>{
    Optional<UserEntity> findByEmail(String email);

}
