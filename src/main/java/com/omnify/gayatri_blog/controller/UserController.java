package com.omnify.gayatri_blog.controller;

import com.omnify.gayatri_blog.model.User;
import com.omnify.gayatri_blog.service.UserService;
import com.omnify.gayatri_blog.util.BadRequestError;
import com.omnify.gayatri_blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)  // Important for security - prevents JS access
                .secure(false)    // Send only over HTTPS
                .path("/")       // Available to all paths
                .maxAge(7 * 24 * 60 * 60)  // 7 days expiration
                .sameSite("Lax") // Helps with CSRF protection
                .build();

        // Set cookie in headers
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
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
                .path("/")       // Available to all paths
                .httpOnly(true)  // Important for security - prevents JS access
                .secure(false)    // Send only over HTTPS
                .maxAge(7 * 24 * 60 * 60)  // 7 days expiration
                .sameSite("Lax") // Helps with CSRF protection
                .build();

        // Set cookie in headers
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
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
