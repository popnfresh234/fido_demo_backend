package com.example.apilogin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Email is mandatory")
    private  String email;

    @NotBlank(message= "Name is mandatory")
    private String name;

    @JsonIgnore
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Birthdate is mandatory")
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
