package com.example.apilogin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(columnDefinition = "DATE")
    private LocalDate birthdate;

//    Address
    private String city;
    private String district;
    private String street;
    private String alley;
    private String lane;
    private String floor;
//    End address

    private String role;
    private String extraInfo;

}
