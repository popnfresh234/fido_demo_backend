package com.example.apilogin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  Long id;
    @Column(unique = true)
    private  String email;
    private String name;
    @JsonIgnore
    private String password;
    private String role;
    private String extraInfo;

}
