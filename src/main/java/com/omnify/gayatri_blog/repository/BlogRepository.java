package com.omnify.gayatri_blog.repository;

import com.omnify.gayatri_blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByIdAndUserId(Long Id, Long userId);
    List<Blog> findByUserId(Long userId);
}
