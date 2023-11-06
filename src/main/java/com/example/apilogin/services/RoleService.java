package com.example.apilogin.services;

import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void save(RoleEntity roleEntity){
        roleRepository.save(roleEntity);
    }
}
