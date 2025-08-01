package com.omnify.gayatri_blog.service;

import com.omnify.gayatri_blog.model.User;

public interface UserService {
    User registerUser(User u);
    User loginUser(String email, String password);
    User getUser(Long id);
}
