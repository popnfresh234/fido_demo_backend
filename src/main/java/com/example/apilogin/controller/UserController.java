package com.example.apilogin.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.Response;
import com.example.apilogin.security.JwtDecoder;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.utils.ImageUtils;
import com.example.apilogin.utils.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping(path = "/user")
@RequiredArgsConstructor

public class UserController {
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        log.info("GET /user/all");
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/")
    public @ResponseBody UserEntity getUser(@RequestParam Integer id) {
        log.info("GET /user/:id");
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataAccessException("This user cannot be found") {
            };
        }
    }

    @PostMapping(path = "/")
    public @ResponseBody UserEntity editUser(

            @RequestParam
            @NotEmpty(message = "ID must not be empty")
            Integer id,

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
            @RequestParam("image") MultipartFile file

    ) throws IOException {
        log.info("POST /user");
        Optional<UserEntity> user = userRepository.findById((id));
        if (user.isPresent()) {
            UserEntity foundUser = user.get();
            setUserData(foundUser, name, birthdate, city, district, street, alley, lane, floor);
            if (file.getSize() > 0) {
                foundUser.setImage(file.getBytes());
            }
            return userRepository.save(foundUser);
        } else {
            throw new DataAccessException("Something went wrong updating a user") {
            };
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
