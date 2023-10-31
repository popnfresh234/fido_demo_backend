package com.example.apilogin.controller;

import com.example.apilogin.entities.PasswordResetEntity;
import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.*;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.PasswordResetRepository;
import com.example.apilogin.service.RoleRepository;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.utils.MailUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin
@Validated
@Log4j2
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private MailUtils mailUtils;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        log.info("POST /login");
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));
        var principal = (UserPrincipal) authentication.getPrincipal();
        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        var token = jwtIssuer.issue(principal.getUserId(), principal.getAccount(), roles);
        return new LoginResponse("Login Success", token, roles);
    }

    @PostMapping("/signup")
    public Response signup(

            @Valid
            @RequestParam
            @NotEmpty(message = "Account number must not be empty")
            @Size(min = 2, message = "Account number must be at least two chars")
            @Size(max = 20, message = "Account number must not be greater than 20 chars")
            @Pattern(regexp = "^[a-zA-Z0-9]*$")
            String account,

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
            @RequestParam String floor,
            @RequestParam("image") MultipartFile file
    ) throws IOException {


        log.info("POST /signup");
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            log.error("POST /signup User Already Exists");
            throw new DataAccessException("This user already exists") {
            };
        } else {
            var user = new UserEntity();
            user.setAccount(account);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            UserController.setUserData(user, name, birthdate, city, district, street, alley, lane, floor);
            user.setExtraInfo(("A user"));
            var userRole = new RoleEntity();
            userRole.setRole("ROLE_USER");
            roleRepository.save(userRole);
            user.getRole().add(userRole);
            if (file.getSize() > 0) {
                user.setImage(file.getBytes());
            }
            userRepository.save(user);

            return new SignupResponse("New user added!");
        }
    }

    @PostMapping(path = "/recovery")
    public Response recoverAccount(
            @RequestParam
            @NotEmpty(message = "Account number must not be empty")
            @Size(min = 2, message = "Account number must be at least two chars")
            @Size(max = 20, message = "Account number must not be greater than 20 chars")
            @Pattern(regexp = "^[a-zA-Z0-9]*$")
            String account) {
        log.info("POST /recovery");
        Optional<UserEntity> userOptional = userRepository.findByAccount(account);
        if (userOptional.isPresent()) {
//            Create password recovery code
            UserEntity user = userOptional.get();
            UUID code = UUID.randomUUID();
            PasswordResetEntity resetEntity = new PasswordResetEntity();
            LocalDateTime ldt = LocalDateTime.now().plusSeconds(60*5);
            resetEntity.setExpiry(ldt);
            resetEntity.setToken(code.toString());
            passwordResetRepository.save(resetEntity);
            user.setReset(resetEntity);
            userRepository.save(user);

//            Send recovery email to user
            String subject = "Forgotten Password";
            String message = "Hello " + user.getName() + ", you seem to have forgotten your password! Use " + code + " to reset your password";
            mailUtils.sendEmail(user.getEmail(), subject, message);
            log.info("Found a user, should send recovery email to: " + user.getEmail());
            return new Response("Found a user, should send recovery email to:" + user.getEmail());
        } else {
            throw new DataAccessException("Cannot find a user with this ID") {
            };
        }
    }

    @PostMapping(path="/recovery/verify")
    public Response verifyReset(@RequestBody RecoveryRequest request){
        log.info("POST/ verify token");
        Optional<UserEntity> user = userRepository.findByAccount(request.getAccount());
        if(user.isPresent()){
            UserEntity recoverUser = user.get();
            PasswordResetEntity resetEntity = recoverUser.getReset();
            if(resetEntity == null){
                throw new RuntimeException("No recovery code");
            }
            LocalDateTime expiry = resetEntity.getExpiry();
            if(resetEntity.getToken().equals(request.getCode())&& expiry.isAfter(LocalDateTime.now()) ){
                return new RecoveryResponse("Valid code", request.getAccount(), request.getCode());
            } else throw new RuntimeException("Code not valid");
        }else {
            throw new EntityNotFoundException("Cannot find user");
        }
    }

    @PostMapping(path = "/recovery/reset")
    public Response updatePassword(@RequestBody ResetRequest request){
        Optional<UserEntity> opUser = userRepository.findByAccount(request.getAccount());
        if(opUser.isPresent()){
            UserEntity userEntity = opUser.get();;
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            PasswordResetEntity reset = userEntity.getReset();
            userEntity.setReset(null);
            userRepository.save(userEntity);

            passwordResetRepository.delete(reset);
            return new Response("Success");
        } else {
            throw new EntityNotFoundException("Cannot find user");
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
