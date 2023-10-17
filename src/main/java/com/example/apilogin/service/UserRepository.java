package com.example.apilogin.service;
import com.example.apilogin.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;



public interface UserRepository extends CrudRepository<UserEntity, Integer>{
    UserEntity findByEmail(String email);

}
