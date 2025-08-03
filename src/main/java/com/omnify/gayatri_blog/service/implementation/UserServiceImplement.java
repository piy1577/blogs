package com.omnify.gayatri_blog.service.implementation;

import com.omnify.gayatri_blog.model.User;
import com.omnify.gayatri_blog.repository.UserRepository;
import com.omnify.gayatri_blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplement implements UserService{

    @Autowired
    private UserRepository ur;

    @Autowired
    private PasswordEncoder bcrypt;

    @Override
    public User registerUser(User u) {
        if(ur.findFirstByEmail(u.getEmail()).isPresent()){
            throw new RuntimeException("User already Exists");
        }
        u.setPassword(bcrypt.encode(u.getPassword()));
        return ur.save(u);
    }

    @Override
    public User loginUser(String email, String password) {
        User u = ur.findFirstByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(!bcrypt.matches(password, u.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return u;
    }

    @Override
    public User getUser(Long id) {
        return ur.findById(id).get();
    }
}
