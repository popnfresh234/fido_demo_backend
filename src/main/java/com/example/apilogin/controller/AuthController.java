package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.*;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@Validated

@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        System.out.println(request.getEmail());
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var principal = (UserPrincipal) authentication.getPrincipal();
        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return new LoginResponse("Login Success", token);
    }

    @PostMapping("/signup")
    public Response signup(
            @Valid
            @RequestParam
            @NotEmpty(message = "Name must not be empty")
            @Size(min = 1, message = "Name must be at least 1 char")
            @Size(max = 20, message = "Name must not be greater than 20 chars")
            String name,

            @NotEmpty(message = "Email must not be empty")
            @Size(max = 50, message = "Email must be less than 50 chars")
            @Valid @RequestParam String email,

            @Valid
            @RequestParam
            @NotEmpty(message = "Password must not be empty")
            @Size(min = 8, message = "Password must be at least 8 chars")
            @Size(max = 20, message = "Password must not be greater than 20 chars")
            @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,12}$", message = "Password must meet reqs")
            String password,

            @NotEmpty(message = "Email must not be empty")

            @Valid
            @RequestParam
            @NotEmpty(message = "Date must not be empty")
            @Pattern(regexp = "^[12][0-9][0-9][0-9]/[01][0-9]/[0-3][0-9]$", message = "Date must match format yyyy/MM/dd")
            String birthdate,


            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String street,
            @RequestParam String alley,
            @RequestParam String lane,
            @RequestParam String floor) {
        var user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(birthdate, formatter);
        user.setBirthdate(date);
        user.setCity(city);
        user.setDistrict(district);
        user.setStreet(street);
        user.setAlley(alley);
        user.setLane(lane);
        user.setFloor(floor);
        user.setRole("ROLE_ADMIN");
        user.setExtraInfo(("My nice admin"));

        Optional<UserEntity> foundUser = userRepository.findByEmail(email);
        if(foundUser.isPresent()){
            throw new DataAccessException("This user already exists") {};
        } else {
            userRepository.save(user);
            return new SignupResponse("New user added!");
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException e) {

        List<String> errorMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(errorMessages.toString()));

    }
}
