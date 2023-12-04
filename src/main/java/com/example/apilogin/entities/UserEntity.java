package com.example.apilogin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(unique = true)
    @NotBlank(message = "Account number is mandatory")
    private String account;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private Set<RoleEntity> role = new HashSet<>();

    private String extraInfo;

    @Lob
    @Column(length=100000)
    private byte[] image;

    private String imageName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private PasswordResetEntity reset;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private Set<UserLogEntity> logs = new HashSet<>();
}
