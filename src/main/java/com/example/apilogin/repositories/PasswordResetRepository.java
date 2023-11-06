package com.example.apilogin.repositories;

import com.example.apilogin.entities.PasswordResetEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetRepository extends CrudRepository<PasswordResetEntity, Integer> {
}
