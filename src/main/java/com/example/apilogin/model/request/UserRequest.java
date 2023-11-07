package com.example.apilogin.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@Data
public class UserRequest {

    @NotEmpty(message = "Account number must not be empty")
    @Size(min = 2, message = "Account number must be at least two chars")
    @Size(max = 20, message = "Account number must not be greater than 20 chars")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String account;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 1, message = "Name must be at least 1 char")
    @Size(max = 20, message = "Name must not be greater than 20 chars")
    private String name;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 chars")
    @Size(max = 20, message = "Password must not be greater than 20 chars")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,12}$", message = "Password must meet reqs")
    private String password;

    @NotEmpty(message = "Date must not be empty")
    @Pattern(regexp = "^[12][0-9][0-9][0-9]/[01][0-9]/[0-3][0-9]$", message = "Date must match format yyyy/MM/dd")
    private String birthdate;

    private String city;

    private String district;

    private String street;

    private String alley;

    private String lane;

    private String floor;

    private MultipartFile image;
}
