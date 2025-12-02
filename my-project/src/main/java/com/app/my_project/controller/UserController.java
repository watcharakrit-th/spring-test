package com.app.my_project.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.WebConfig;
import com.app.my_project.entity.UserEntity;
import com.app.my_project.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    @PostMapping("/signin")
    public Object sigin(@RequestBody UserEntity user) {
        try {
            String u = user.getUsername();
            String p = user.getPassword();

            UserEntity userForCreateToken = userRepository.findByUsernameAndPassword(u, p);

            String token = JWT.create()
                    .withSubject(String.valueOf(userForCreateToken.getId()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withIssuedAt(new Date())
                    .sign(Algorithm.HMAC256(WebConfig.getSecret()));

            String role = userForCreateToken.getRole();

            record UserResponse(String token, String role) {
            }
            return new UserResponse(token, role);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error creating token");
        }
    }

}
