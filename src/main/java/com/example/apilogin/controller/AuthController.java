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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                    "login",
                    true
            );
            user.getLogs().add(log);
            userRepository.save(user);
            httpServletRequest.setAttribute("status", "success");

            return new LoginResponse("Login Success", token, roles);
        } catch (Exception e) {
            throw AuthException.builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_LOGIN)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(request.getAccount())
                    .build();
        }
    }

    @PostMapping("/signup")
    public Response signup(@ModelAttribute @Valid UserRequest signupRequest, HttpServletRequest httpServletRequest) {
        log.info("POST /signup");
        Optional<UserEntity> foundUser = userRepository.findByEmail(signupRequest.getEmail());

//        If this user already exists in the database, throw an error
        if (foundUser.isPresent()) {
            log.error("POST /signup User Already Exists");
            throw AuthException
                    .builder()
                    .msg("User already exists")
                    .operation(OPERATION_SIGNUP)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(signupRequest.getAccount())
                    .build();
        }

//        Try to save the user
        try {
            var user = new UserEntity();
            user.setAccount(signupRequest.getAccount());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            UserController.setUserData(user, signupRequest.getName(), signupRequest.getBirthdate(), signupRequest.getCity(), signupRequest.getDistrict(), signupRequest.getStreet(), signupRequest.getAlley(), signupRequest.getAlley(), signupRequest.getFloor());
            RoleEntity userRole = new RoleEntity();
            userRole.setRole("ROLE_USER");
            roleRepository.save(userRole);
            user.getRole().add(userRole);
            user.setImage(signupRequest.getImage().getBytes());
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

            throw AuthException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_SIGNUP)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(signupRequest.getAccount())
                    .build();
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
            UserLogEntity userLog = LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_RECOVERY_REQUEST,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Recovery Request",
                    true);
            user.getLogs().add(userLog);
            userRepository.save(user);

//            Send recovery email to user
            String subject = "Forgotten Password";
            String message = "Hello " + user.getName() + ", you seem to have forgotten your password! Use " + code + " to reset your password";
            mailUtils.sendEmail(user.getEmail(), subject, message);
            log.info("Found a user, should send recovery email to: " + user.getEmail());
            return new Response("Found a user, should send recovery email to:" + user.getEmail());
        } catch (Exception e) {
            throw AuthException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(account)
                    .build();
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
            throw AuthException.builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_RECOVERY_VERIFY)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(request.getAccount())
                    .build();
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
            throw AuthException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(request.getAccount())
                    .build();
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
