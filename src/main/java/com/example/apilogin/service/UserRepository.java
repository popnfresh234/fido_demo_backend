package com.example.apilogin.service;
import com.example.apilogin.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository

public interface UserRepository extends CrudRepository<UserEntity, Integer>{
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByAccount(String account);

}
