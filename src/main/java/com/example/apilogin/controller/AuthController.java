package com.example.apilogin.controller;

import com.example.apilogin.entities.PasswordResetEntity;
import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.exceptions.RecoveryException;
import com.example.apilogin.model.request.*;
import com.example.apilogin.model.request.recovery.VerifyRecoveryRequest;
import com.example.apilogin.model.request.recovery.RecoveryRequest;
import com.example.apilogin.model.request.recovery.ResetPasswordRequest;
import com.example.apilogin.model.response.*;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.services.PasswordResetService;
import com.example.apilogin.services.RoleService;
import com.example.apilogin.services.UserLogService;
import com.example.apilogin.services.UserService;
import com.example.apilogin.utils.LogUtils;
import com.example.apilogin.utils.MailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping(path = "/auth")
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordResetService passwordResetService;
    private final UserLogService userLogService;
    private final MailUtils mailUtils;


    public AuthController(
            JwtIssuer jwtIssuer,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserService userService,
            RoleService roleService,
            PasswordResetService passwordResetService,
            UserLogService userLogService,
            MailUtils mailUtils) {
        this.jwtIssuer = jwtIssuer;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordResetService = passwordResetService;
        this.userLogService = userLogService;
        this.mailUtils = mailUtils;
    }


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody @Validated LoginRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            log.info(LogUtils.buildRouteLog("POST /login"));
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getAccount(),
                    request.getPassword()));
            var principal = (UserPrincipal) authentication.getPrincipal();
            var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            var token = jwtIssuer.issue(
                    principal.getUserId(),
                    principal.getAccount(),

                    principal.getName(),
                    principal.getEmail(),
                    roles);
//        Log user logging in
            Optional<UserEntity> opt = userService.findByAccount(request.getAccount());
            UserEntity user = opt.orElseThrow();
            UserLogEntity log = LogUtils.buildLog(
                    userLogService,
                    LogUtils.OPERATION_LOGIN,
                    request.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "login",
                    true);
            user.getLogs().add(log);
            userService.save(user);
            httpServletRequest.setAttribute(
                    "status",
                    "success");

            return new LoginResponse(
                    "Login Success",
                    token,
                    roles);
        } catch (Exception e) {
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.OPERATION_LOGIN)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }

    @PostMapping("/signup")
    public Response signup(
            @ModelAttribute @Valid UserRequest signupRequest,
            HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /signup"));
        Optional<UserEntity> foundUser = userService.findByEmail(signupRequest.getEmail());

//        If this user already exists in the database, throw an error
        if (foundUser.isPresent()) {
            log.error("POST /signup User Already Exists");
            throw AuthException.builder().msg("User already exists").operation(LogUtils.OPERATION_SIGNUP)
                    .ip(httpServletRequest.getRemoteAddr()).target(signupRequest.getAccount()).build();
        }

//        Try to save the user
        try {
            var user = new UserEntity();
            user.setAccount(signupRequest.getAccount());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            UserController.setUserData(
                    user,
                    signupRequest.getName(),
                    signupRequest.getBirthdate(),
                    signupRequest.getCity(),
                    signupRequest.getDistrict(),
                    signupRequest.getStreet(),
                    signupRequest.getAlley(),
                    signupRequest.getAlley(),
                    signupRequest.getFloor());
            RoleEntity userRole = new RoleEntity();
            userRole.setRole("ROLE_USER");
            roleService.save(userRole);
            user.getRole().add(userRole);
            user.setImage(signupRequest.getImage().getBytes());
//            Log new user activity
            UserLogEntity log = LogUtils.buildLog(
                    userLogService,
                    LogUtils.OPERATION_SIGNUP,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Created user",
                    true);
            user.getLogs().add(log);
            userService.save(user);
            return new SignupResponse("New user added!");
        } catch (Exception e) {

            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.OPERATION_SIGNUP)
                    .ip(httpServletRequest.getRemoteAddr()).target(signupRequest.getAccount()).build();
        }
    }

    @PostMapping(path = "/recovery")
    public Response recoverAccount(
            @RequestBody @Valid RecoveryRequest req,
            HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /recovery"));
        Optional<UserEntity> userOptional = userService.findByAccount(req.getAccount());
//            Create password recovery code
        try {
            UserEntity user = userOptional.orElseThrow();
            UUID code = UUID.randomUUID();
            PasswordResetEntity resetEntity = new PasswordResetEntity();
            LocalDateTime ldt = LocalDateTime.now().plusSeconds(60 * 5);
            resetEntity.setExpiry(ldt);
            resetEntity.setToken(code.toString());
            passwordResetService.save(resetEntity);
            user.setReset(resetEntity);
//            Log success
            UserLogEntity userLog = LogUtils.buildLog(
                    userLogService,
                    LogUtils.OPERATION_RECOVERY_REQUEST,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Recovery Request",
                    true);
            user.getLogs().add(userLog);
            userService.save(user);

//            Send recovery email to user
            String subject = "Forgotten Password";
            String message = "Hello " + user.getName() + ", you seem to have forgotten your password! Use " + code + " to reset your password";
            mailUtils.sendEmail(
                    user.getEmail(),
                    subject,
                    message);
            log.info("Found a user, should send recovery email to: " + user.getEmail());
            return new Response("Found a user, should send recovery email to:" + user.getEmail());
        } catch (Exception e) {
            throw RecoveryException.builder().msg(e.getMessage()).operation(LogUtils.OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr()).target(req.getAccount()).build();
        }
    }

    @PostMapping(path = "/recovery/verify")
    public Response verifyReset(
            @RequestBody @Valid VerifyRecoveryRequest request,
            HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog( "POST/ auth/recovery/verify"));
        Optional<UserEntity> user = userService.findByAccount(request.getAccount());
        try {
            UserEntity recoverUser = user.orElseThrow();
            PasswordResetEntity resetEntity = recoverUser.getReset();
            if (resetEntity != null && resetEntity.getToken().equals(request.getCode()) && resetEntity.getExpiry()
                    .isAfter(LocalDateTime.now())) {
                UserLogEntity log = LogUtils.buildLog(
                        userLogService,
                        LogUtils.OPERATION_RECOVERY_VERIFY,
                        request.getAccount(),
                        httpServletRequest.getRemoteAddr(),
                        "Recovery Code Verification",
                        true);
                recoverUser.getLogs().add(log);
                userService.save(recoverUser);
                return new RecoveryResponse(
                        "Valid code",
                        request.getAccount(),
                        request.getCode());
            } else {
                throw new Exception("Invalid recovery code");
            }
        } catch (Exception e) {
            throw RecoveryException.builder().msg(e.getMessage()).operation(LogUtils.OPERATION_RECOVERY_VERIFY)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }

    @PostMapping(path = "/recovery/reset")
    public Response updatePassword(
            @RequestBody @Valid ResetPasswordRequest request,
            HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog( "POST /recovery/reset"));
        try {
            Optional<UserEntity> opUser = userService.findByAccount(request.getAccount());
            UserEntity userEntity = opUser.orElseThrow();
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            PasswordResetEntity reset = userEntity.getReset();
            userEntity.setReset(null);

            UserLogEntity log = LogUtils.buildLog(
                    userLogService,
                    LogUtils.OPERATION_RECOVERY_RESET,
                    userEntity.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Reset password",
                    true);

            userEntity.getLogs().add(log);
            userService.save(userEntity);
            passwordResetService.delete(reset);
            return new Response("Success");
        } catch (Exception e) {
            throw RecoveryException.builder().msg(e.getMessage()).operation(LogUtils.OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException e) {
        List<String> errorMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(errorMessages.toString()));
    }
}
