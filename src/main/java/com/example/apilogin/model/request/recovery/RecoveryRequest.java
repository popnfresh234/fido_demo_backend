package com.example.apilogin.model.request.recovery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecoveryRequest {
    @NotEmpty(message = "Account number must not be empty")
    @Size(min = 2, message = "Account number must be at least two chars")
    @Size(max = 20, message = "Account number must not be greater than 20 chars")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String account;
}
