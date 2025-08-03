package com.omnify.gayatri_blog.service.implementation;

import com.omnify.gayatri_blog.model.Blog;
import com.omnify.gayatri_blog.model.User;
import com.omnify.gayatri_blog.repository.BlogRepository;
import com.omnify.gayatri_blog.repository.UserRepository;
import com.omnify.gayatri_blog.service.BlogService;
import com.omnify.gayatri_blog.util.NotFoundError;
import com.omnify.gayatri_blog.util.UnauthorizedError;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImplement implements BlogService {

    @Autowired
    BlogRepository br;
    @Autowired
    UserRepository ur;

    @Override
    public Blog createBlog(Blog b, Long id) {
        User saved = ur.findById(id).orElseThrow(() -> new UnauthorizedError("User not found"));
        b.setUser(saved);
        return br.save(b);
    }

    @Override
    public List<Blog> getAllBlog(Pageable page) {
        Page<Blog> b = br.findAll(page);
        return b.getContent();
    }

    @Override
    public Blog getBlogById(Long id, Long user_id) {
        return br.findByIdAndUserId(id, user_id).orElseThrow(() -> new NotFoundError("Blog not found"));
    }

    @Override
    public Blog updateBlogById(Blog b, Long user_id) {
        Blog saved =br.findByIdAndUserId(b.getId(), user_id).orElseThrow(() -> new NotFoundError("Blog not found"));
        saved.setTitle(b.getTitle());
        saved.setContent(b.getContent());
        return br.save(saved);
    }

    @Override
    public void deleteBlogById(Long Id, Long user_id) {
        Blog saved =br.findByIdAndUserId(Id, user_id).orElseThrow(() -> new NotFoundError("Blog not found"));
        br.deleteById(saved.getId());
    }

    @Override
    public List<Blog> getBlogByUser(Long user_id) {
        return br.findByUserId(user_id);
    }
}
