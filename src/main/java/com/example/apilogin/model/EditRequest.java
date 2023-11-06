package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class EditRequest {
    private String account;
    private String name;
    private String birthdate;
    private String city;
    private String district;
    private String street;
    private String alley;
    private String lane;
    private String floor;
    private MultipartFile image;
}
