package com.example.apilogin.model.request.recovery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
public class ResetPasswordRequest {
    @NotEmpty(message = "Account number must not be empty")
    @Size(min = 2, message = "Account number must be at least two chars")
    @Size(max = 20, message = "Account number must not be greater than 20 chars")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private final String account;


    @NotEmpty(message="Recovery code must not be empty")
    private final String code;

    @NotEmpty(message="Password must not be empty")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,12}$", message = "Password must meet reqs")
    private final String password;
}
