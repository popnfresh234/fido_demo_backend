package com.example.apilogin.controller;

import com.example.apilogin.entities.PasswordResetEntity;
import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.request.*;
import com.example.apilogin.model.response.*;
import com.example.apilogin.model.webauthn.request.Fido2DoAuthReq;
import com.example.apilogin.model.webauthn.request.Fido2DoRegReq;
import com.example.apilogin.model.webauthn.request.Fido2RequestAuthReq;
import com.example.apilogin.model.webauthn.request.Fido2RequestRegReq;
import com.example.apilogin.model.webauthn.response.Fido2DoAuthResp;
import com.example.apilogin.model.webauthn.response.Fido2DoRegResp;
import com.example.apilogin.model.webauthn.response.Fido2RequestAuthResp;
import com.example.apilogin.model.webauthn.response.Fido2RequestRegResp;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.services.*;
import com.example.apilogin.utils.LogUtils;
import com.example.apilogin.utils.MailUtils;
import com.example.apilogin.utils.UserSingleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


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
    private final WebauthnService webauthnService;
    private final MailUtils mailUtils;

    private static final String OPERATION_LOGIN = "login";
    private static final String OPERATION_SIGNUP = "signup";
    private static final String OPERATION_RECOVERY_REQUEST = "recovery_request";
    private static final String OPERATION_RECOVERY_VERIFY = "recovery_verify";
    private static final String OPERATION_RECOVERY_RESET = "recovery_reset";

    @Value("${fido.origin}")
    private String fidoOrigin;

    @Value("${fido.rp-id}")
    private String fidoRpId;


    public AuthController(
            JwtIssuer jwtIssuer,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserService userService,
            RoleService roleService,
            PasswordResetService passwordResetService,
            UserLogService userLogService,
            WebauthnService webauthnService,
            MailUtils mailUtils) {
        this.jwtIssuer = jwtIssuer;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordResetService = passwordResetService;
        this.userLogService = userLogService;
        this.webauthnService = webauthnService;
        this.mailUtils = mailUtils;
    }


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody @Validated LoginRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            log.info("POST /login");
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
                    OPERATION_LOGIN,
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
            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_LOGIN)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }

    @PostMapping("/signup")
    public Response signup(
            @ModelAttribute @Valid UserRequest signupRequest,
            HttpServletRequest httpServletRequest) {
        log.info("POST /signup");
        Optional<UserEntity> foundUser = userService.findByEmail(signupRequest.getEmail());

//        If this user already exists in the database, throw an error
        if (foundUser.isPresent()) {
            log.error("POST /signup User Already Exists");
            throw AuthException.builder().msg("User already exists").operation(OPERATION_SIGNUP)
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
                    OPERATION_SIGNUP,
                    user.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Created user",
                    true);
            user.getLogs().add(log);
            userService.save(user);
            return new SignupResponse("New user added!");
        } catch (Exception e) {

            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_SIGNUP)
                    .ip(httpServletRequest.getRemoteAddr()).target(signupRequest.getAccount()).build();
        }
    }

    @PostMapping(path = "/recovery")
    public Response recoverAccount(
            @RequestParam @NotEmpty(message = "Account number must not be empty") @Size(min = 2, message = "Account number must be at least two chars") @Size(max = 20, message = "Account number must not be greater than 20 chars") @Pattern(regexp = "^[a-zA-Z0-9]*$") String account,
            HttpServletRequest httpServletRequest) {
        log.info("POST /recovery");
        Optional<UserEntity> userOptional = userService.findByAccount(account);
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
                    OPERATION_RECOVERY_REQUEST,
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
            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr()).target(account).build();
        }
    }

    @PostMapping(path = "/recovery/verify")
    public Response verifyReset(
            @RequestBody RecoveryRequest request,
            HttpServletRequest httpServletRequest) {
        log.info("POST/ verify token");
        Optional<UserEntity> user = userService.findByAccount(request.getAccount());
        try {
            UserEntity recoverUser = user.orElseThrow();
            PasswordResetEntity resetEntity = recoverUser.getReset();
            if (resetEntity != null && resetEntity.getToken().equals(request.getCode()) && resetEntity.getExpiry()
                    .isAfter(LocalDateTime.now())) {
                UserLogEntity log = LogUtils.buildLog(
                        userLogService,
                        OPERATION_RECOVERY_VERIFY,
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
                throw new Exception("Bad token");
            }
        } catch (Exception e) {
            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_RECOVERY_VERIFY)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }

    @PostMapping(path = "/recovery/reset")
    public Response updatePassword(
            @RequestBody ResetRequest request,
            HttpServletRequest httpServletRequest) {
        log.info("POST /recovery/reset");
        try {
            Optional<UserEntity> opUser = userService.findByAccount(request.getAccount());
            UserEntity userEntity = opUser.orElseThrow();
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            PasswordResetEntity reset = userEntity.getReset();
            userEntity.setReset(null);

            UserLogEntity log = LogUtils.buildLog(
                    userLogService,
                    OPERATION_RECOVERY_RESET,
                    userEntity.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Reset password",
                    true);

            userEntity.getLogs().add(log);
            userService.save(userEntity);
            passwordResetService.delete(reset);
            return new Response("Success");
        } catch (Exception e) {
            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_RECOVERY_REQUEST)
                    .ip(httpServletRequest.getRemoteAddr()).target(request.getAccount()).build();
        }
    }

    @PostMapping(path = "/requestReg")
    public Fido2RequestRegResp requestReg(
            @RequestBody Fido2RequestRegReq req,
            HttpServletRequest httpServletRequest) {
        log.info("POST /requestReg");

        try {
            req.getBody().setUsername(LogUtils.getPrincipal().getUsername());
            req.getBody().setDisplayName(LogUtils.getPrincipal().getName());
            req.getBody().setOrigin(fidoOrigin);
            req.getBody().setRpId(fidoRpId);
            req.getBody().setRpName("Fido Lab Relying Party");
            return webauthnService.requestReg(req);
        } catch (Exception e) {
            throw AuthException.builder().msg(e.getMessage()).operation(OPERATION_LOGIN)
                    .ip(httpServletRequest.getRemoteAddr()).target(LogUtils.getUserAccount()).build();
        }
    }

    @PostMapping(path = "/doReg")
    public Fido2DoRegResp doReg(@RequestBody Fido2DoRegReq req) {
        return webauthnService.doReg(req);
    }

    @PostMapping(path = "/requestAuth")
    public Fido2RequestAuthResp requestAuth(@RequestBody Fido2RequestAuthReq req) {
        String username = req.getBody().getUsername();
        UserSingleton.getInstance().setUsername(username);
        req.getBody().setOrigin(fidoOrigin);
        req.getBody().setRpId(fidoRpId);
        return webauthnService.requestAuth(req);
    }


    @PostMapping(path = "/doAuth")
    public Fido2DoAuthResp doAuth(@RequestBody Fido2DoAuthReq req) {

        Fido2DoAuthResp res = webauthnService.doAuth(req);

        if (res.getHeader().getCode().equals("1200")) {
            try {
                // Look up the user by account
                Optional<UserEntity> opt = userService.findByAccount(UserSingleton.getInstance().getUsername());
                UserEntity user = opt.orElseThrow();

                // Login user with correct roles
                Set<RoleEntity> roles = user.getRole();
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (RoleEntity role : roles) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                    authorities.add(authority);
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getAccount(),
                        "",
                        authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Issue JWT for login flow
                List<String> stringAuths = new ArrayList<>();
                for (SimpleGrantedAuthority auth : authorities) {
                    String authority = auth.getAuthority();
                    stringAuths.add(authority);
                }
                var token = jwtIssuer.issue(
                        user.getId(),
                        user.getAccount(),
                        user.getName(),
                        user.getEmail(),
                        stringAuths);

                // Add the login response to the fido response for frontend
                res.setLoginResponse(new LoginResponse("Login Success",
                                                       token,
                                                       stringAuths));

                //Clear out singleton
                UserSingleton.getInstance().setUsername("");
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            log.error("FAIL");
        }


        return res;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException e) {
        List<String> errorMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(errorMessages.toString()));
    }
}
