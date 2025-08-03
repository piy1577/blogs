package com.omnify.gayatri_blog.service;

import com.omnify.gayatri_blog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BlogService {
    Blog createBlog(Blog b,Long id);
    List<Blog> getAllBlog(Pageable page);
    Blog getBlogById(Long id, Long user_id);
    Blog updateBlogById(Blog b, Long user_id);
    void deleteBlogById(Long Id, Long user_id);
    List<Blog> getBlogByUser(Long user_id);
}
