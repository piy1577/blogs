package com.omnify.gayatri_blog.controller;

import com.omnify.gayatri_blog.model.User;
import com.omnify.gayatri_blog.service.UserService;
import com.omnify.gayatri_blog.util.BadRequestError;
import com.omnify.gayatri_blog.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = {"http://dummy-bucket-69.s3-website.ap-south-1.amazonaws.com", "http://localhost:3000"}, allowCredentials = "true")
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
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // Set to true if using HTTPS
                .path("/")
                .maxAge(60 * 60 * 24 * 7) // 7 days
                .build();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, cookie.toString());

        // Set cookie in headers
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(header)
                .body(Map.of(
                        "email", savedUser.getEmail(),
                        "name", savedUser.getName()
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
                        "name", savedUser.getName()
                ));
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getUser(@CookieValue(name="token", required = false) String token){
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Error", "Token not found"));
        }
        Long Id = j.extractId(token);
        User u = us.getUser(Id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("name",u.getName(), "email", u.getEmail() ));
    }


}
