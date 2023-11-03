package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.GeneralException;
import com.example.apilogin.exceptions.UserEditException;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserLogRepository;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping(path = "/user")
@RequiredArgsConstructor

public class UserController {
    private static final String OPERATION_EDIT_USER = "edit user";

    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserLogRepository userLogRepository;

    @GetMapping()
    public @ResponseBody UserEntity getUser() {
        log.info("GET /user/");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        UserPrincipal principal = jwtToPrincipalConverter.convert(token);
        Optional<UserEntity> user = userRepository.findByAccount(principal.getAccount());
        if (user.isPresent()) {
            return user.get();
        } else {
            throw GeneralException.builder().msg("User not found").build();
        }
    }

    @PostMapping(path = "/")
    public @ResponseBody UserEntity editUser(

            @RequestParam
            @NotEmpty(message = "Account must not be empty")
            String account,

            @Valid
            @RequestParam
            @NotEmpty(message = "Name must not be empty")
            @Size(min = 1, message = "Name must be at least 1 char")
            @Size(max = 20, message = "Name must not be greater than 20 chars")
            String name,


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
        log.info("POST /user");
        try {
            Optional<UserEntity> user = userRepository.findByAccount(account);
            UserEntity foundUser = user.orElseThrow();
            setUserData(foundUser, name, birthdate, city, district, street, alley, lane, floor);
            foundUser.setImage(file.getBytes());
            UserLogEntity log = LogUtils.buildLog(
                    userLogRepository,
                    OPERATION_EDIT_USER,
                    foundUser.getAccount(),
                    httpServletRequest.getRemoteAddr(),
                    "Edit user details",
                    true
            );
            foundUser.getLogs().add(log);
            return userRepository.save(foundUser);
        } catch (Exception e) {
            throw UserEditException
                    .builder()
                    .msg(e.getMessage())
                    .operation(OPERATION_EDIT_USER)
                    .ip(httpServletRequest.getRemoteAddr())
                    .target(account)
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
