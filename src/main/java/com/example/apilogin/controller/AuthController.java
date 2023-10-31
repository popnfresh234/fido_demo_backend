package com.example.apilogin.controller;

import com.example.apilogin.entities.PasswordResetEntity;
import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.*;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.PasswordResetRepository;
import com.example.apilogin.service.RoleRepository;
import com.example.apilogin.service.UserLogRepository;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.utils.LogUtils;
import com.example.apilogin.utils.MailUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private UserLogRepository userLogRepository;

    @Autowired
    private MailUtils mailUtils;
    private static final String OPERATION_LOGIN = "login";
    private static final String OPERATION_SIGNUP = "signup";
    private static final String OPERATION_RECOVERY_REQUEST = "recovery_request";

    private static final String OPERATION_RECOVERY_VERIFY = "recovery_verify";

    private static final String OPERATION_RECOVERY_RESET = "recovery_reset";


    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request, HttpServletRequest httpServletRequest) {
        try {
            log.info("POST /login");
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));
            var principal = (UserPrincipal) authentication.getPrincipal();
            var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            var token = jwtIssuer.issue(principal.getUserId(), principal.getAccount(), roles);
//        Log user logging in
            Optional<UserEntity> opt = userRepository.findByAccount(request.getAccount());
            UserEntity user = opt.orElseThrow();
            UserLogEntity log = LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_LOGIN,
                    request.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "",
                    true
            );
            user.getLogs().add(log);
            userRepository.save(user);
            return new LoginResponse("Login Success", token, roles);
        } catch (Exception e) {
            AuthException authException = new AuthException(e.getMessage());
            authException.setOperation(OPERATION_LOGIN);
            authException.setIp(httpServletRequest.getRemoteAddr());
            authException.setTarget(request.getAccount());
            throw authException;
        }
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
            @RequestParam("image") MultipartFile file,
            HttpServletRequest httpServletRequest
    ) throws IOException {
        log.info("POST /signup");
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);

//        If this user already exists in the database, throw an error
        if (foundUser.isPresent()) {
            log.error("POST /signup User Already Exists");
            AuthException e = new AuthException("User already exists");
            e.setOperation(OPERATION_SIGNUP);
            e.setIp(httpServletRequest.getRemoteAddr());
            e.setTarget(account);
            throw e;
        }

//        Try to save the user
        try {
            var user = new UserEntity();
            user.setAccount(account);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            UserController.setUserData(user, name, birthdate, city, district, street, alley, lane, floor);
            RoleEntity userRole = new RoleEntity();
            userRole.setRole("ROLE_USER");
            roleRepository.save(userRole);
            user.getRole().add(userRole);
            if (file.getSize() > 0) {
                user.setImage(file.getBytes());
            }

//            Log new user activity
            UserLogEntity log = LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_SIGNUP,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Created user",
                    true);
            user.getLogs().add(log);
            userRepository.save(user);
            return new SignupResponse("New user added!");
        } catch (Exception e) {
            AuthException ex = new AuthException(e.getMessage());
            ex.setOperation(OPERATION_SIGNUP);
            ex.setTarget(account);
            ex.setIp(httpServletRequest.getRemoteAddr());
            throw ex;
        }
    }

    @PostMapping(path = "/recovery")
    public Response recoverAccount(
            @RequestParam
            @NotEmpty(message = "Account number must not be empty")
            @Size(min = 2, message = "Account number must be at least two chars")
            @Size(max = 20, message = "Account number must not be greater than 20 chars")
            @Pattern(regexp = "^[a-zA-Z0-9]*$")
            String account,
            HttpServletRequest httpServletRequest) {
        log.info("POST /recovery");
        Optional<UserEntity> userOptional = userRepository.findByAccount(account);
//            Create password recovery code
        try {
            UserEntity user = userOptional.orElseThrow();
            UUID code = UUID.randomUUID();
            PasswordResetEntity resetEntity = new PasswordResetEntity();
            LocalDateTime ldt = LocalDateTime.now().plusSeconds(60 * 5);
            resetEntity.setExpiry(ldt);
            resetEntity.setToken(code.toString());
            passwordResetRepository.save(resetEntity);
            user.setReset(resetEntity);
//            Log success
            LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_RECOVERY_REQUEST,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Recovery Request",
                    true);
            userRepository.save(user);

//            Send recovery email to user
            String subject = "Forgotten Password";
            String message = "Hello " + user.getName() + ", you seem to have forgotten your password! Use " + code + " to reset your password";
            mailUtils.sendEmail(user.getEmail(), subject, message);
            log.info("Found a user, should send recovery email to: " + user.getEmail());
            return new Response("Found a user, should send recovery email to:" + user.getEmail());
        } catch (Exception e) {
            AuthException ex = new AuthException(e.getMessage());
            ex.setOperation(OPERATION_RECOVERY_REQUEST);
            ex.setIp(httpServletRequest.getRemoteAddr());
            ex.setTarget(account);
            throw ex;
        }
    }

    @PostMapping(path = "/recovery/verify")
    public Response verifyReset(@RequestBody RecoveryRequest request, HttpServletRequest httpServletRequest) {
        log.info("POST/ verify token");
        Optional<UserEntity> user = userRepository.findByAccount(request.getAccount());
        try {
            UserEntity recoverUser = user.orElseThrow();
            PasswordResetEntity resetEntity = recoverUser.getReset();
            if (resetEntity != null &&
                    resetEntity.getToken().equals(request.getCode())
                    && resetEntity.getExpiry().isAfter(LocalDateTime.now())) {
                UserLogEntity log = LogUtils.buildLog(
                        userLogRepository,
                        OPERATION_RECOVERY_VERIFY,
                        request.getAccount(),
                        httpServletRequest.getRemoteAddr(),
                        "Recovery Code Verification",
                        true);
                recoverUser.getLogs().add(log);
                userRepository.save(recoverUser);
                return new RecoveryResponse("Valid code", request.getAccount(), request.getCode());
            } else {
                throw new Exception("Bad token");
            }
        } catch (Exception e) {
            AuthException ex = new AuthException(e.getMessage());
            ex.setIp(httpServletRequest.getRemoteAddr());
            ex.setOperation(OPERATION_RECOVERY_VERIFY);
            ex.setTarget(request.getAccount());
            throw ex;
        }
    }

    @PostMapping(path = "/recovery/reset")
    public Response updatePassword(@RequestBody ResetRequest request, HttpServletRequest httpServletRequest) {
        log.info("POST /recovery/reset");
        try {
            Optional<UserEntity> opUser = userRepository.findByAccount(request.getAccount());
            UserEntity userEntity = opUser.orElseThrow();
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            PasswordResetEntity reset = userEntity.getReset();
            userEntity.setReset(null);

            UserLogEntity log = LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_RECOVERY_RESET,
                    userEntity.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Reset password",
                    true
            );

            userEntity.getLogs().add(log);
            userRepository.save(userEntity);
            passwordResetRepository.delete(reset);
            return new Response("Success");
        } catch (Exception e) {
            AuthException ex = new AuthException(e.getMessage());
            ex.setTarget(request.getAccount());
            ex.setOperation(OPERATION_RECOVERY_RESET);
            ex.setIp(httpServletRequest.getRemoteAddr());
            throw ex;
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
