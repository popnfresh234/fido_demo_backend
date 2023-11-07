package com.example.apilogin.repositories;

import com.example.apilogin.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

}
