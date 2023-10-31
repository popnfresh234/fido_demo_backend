package com.example.apilogin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class UserLogEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  Long id;

    private String operation;

    private String target;

    private String ip;

    private String log;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

    boolean success;
}
