package com.omnify.gayatri_blog.controller;

import com.omnify.gayatri_blog.model.User;
import com.omnify.gayatri_blog.service.UserService;
import com.omnify.gayatri_blog.util.BadRequestError;
import com.omnify.gayatri_blog.util.JwtUtil;
import com.omnify.gayatri_blog.util.UnauthorizedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class UserController {
    @Autowired
    UserService us;

    @Autowired
    JwtUtil j;

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User u) {
        if(u.getEmail() == null || u.getPassword() == null|| u.getName() == null){
            throw new BadRequestError(("Please fill all the required fields"));
        }
        User savedUser = us.registerUser(u);
        String token = j.generateToken(savedUser.getId());
        // Set cookie in headers
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "email", savedUser.getEmail(),
                        "name", savedUser.getName(),
                        "token",token
                ));
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> u) {
        if(u.get("email") == null || u.get("password") == null){
            throw new BadRequestError(("Email and password are required"));
        }

        User savedUser = us.loginUser(u.get("email"), u.get("password"));
        String token = j.generateToken(savedUser.getId());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // Set to true if using HTTPS
                .path("/")
                .maxAge(60 * 60 * 24 * 7) // 7 days
                .build();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, cookie.toString());
        // Set cookie in headers
        return ResponseEntity.ok()
                .headers(header)
                .body(Map.of(
                        "email", savedUser.getEmail(),
                        "name", savedUser.getName(),
                        "token", token
                ));
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getUser(@RequestHeader("Authorization") Optional<String> token){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());
        User u = us.getUser(Id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("name",u.getName(), "email", u.getEmail() ));
    }


}
