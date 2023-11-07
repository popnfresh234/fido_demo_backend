package com.example.apilogin.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(length=100000)
    private String log;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

    boolean success;
}
