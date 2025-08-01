package com.omnify.gayatri_blog.service;

import com.omnify.gayatri_blog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BlogService {
    Blog createBlog(Blog b,Long id);
    Page<Blog> getAllBlog(Pageable page);
    Blog getBlogById(Long id, Long user_id);
    Blog updateBlogById(Blog b, Long user_id);
    void deleteBlogById(Long Id, Long user_id);
}
