package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.GeneralException;
import com.example.apilogin.exceptions.UserException;
import com.example.apilogin.model.request.UserRequest;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.services.UserLogService;
import com.example.apilogin.services.UserService;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping(path = "/user")

public class UserController {
    private static final String OPERATION_EDIT_USER = "edit user";

    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    private final UserService userService;
    private final UserLogService userLogService;

    public UserController(
            UserService userService,
            UserLogService userLogService,
            JwtToPrincipalConverter jwtToPrincipalConverter
    ){
        this.userService = userService;
        this.userLogService = userLogService;
        this.jwtToPrincipalConverter = jwtToPrincipalConverter;
    }

    @GetMapping()
    public @ResponseBody UserEntity getUser() {
        log.info("GET /user/");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        UserPrincipal principal = jwtToPrincipalConverter.convert(token);
        Optional<UserEntity> user = userService.findByAccount(principal.getAccount());
        if (user.isPresent()) {
            return user.get();
        } else {
            throw UserException.builder().msg("User not found").build();
        }
    }

    @PostMapping(path = "/")
    public @ResponseBody UserEntity editUser(

            @ModelAttribute UserRequest editRequest,
            HttpServletRequest httpServletRequest

    ) {
        log.info("POST /user");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt token = (Jwt) authentication.getPrincipal();
            UserPrincipal principal = jwtToPrincipalConverter.convert(token);
            Optional<UserEntity> user = userService.findByAccount(principal.getAccount());
            UserEntity foundUser = user.orElseThrow();
            setUserData(foundUser, editRequest.getName(), editRequest.getBirthdate(), editRequest.getCity(), editRequest.getDistrict(),
                    editRequest.getStreet(), editRequest.getAlley(), editRequest.getLane(), editRequest.getFloor());
            foundUser.setImage(editRequest.getImage().getBytes());
            foundUser.setImageName(editRequest.getImageName());
            log.error(editRequest.getImageName());
            UserLogEntity log = LogUtils.buildLog(
                    userLogService,
                    OPERATION_EDIT_USER,
                    foundUser.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Edit user details",
                    true
            );
            foundUser.getLogs().add(log);
            return userService.save(foundUser);
        } catch (Exception e) {
            throw UserException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_EDIT_USER)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(editRequest.getAccount())
                    .build();
        }
    }

    public static void setUserData(UserEntity user, String name, String birthdate, String city, String district, String street, String alley, String lane, String floor) {
        user.setName(name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(birthdate, formatter);
        user.setBirthdate(date);
        user.setCity(city);
        user.setDistrict(district);
        user.setStreet(street);
        user.setAlley(alley);
        user.setLane(lane);
        user.setFloor(floor);
    }
}
