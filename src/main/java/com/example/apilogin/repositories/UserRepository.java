package com.example.apilogin.repositories;

import com.example.apilogin.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByAccount(String account);
}
