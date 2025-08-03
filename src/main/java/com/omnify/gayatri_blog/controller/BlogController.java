package com.omnify.gayatri_blog.controller;

import com.omnify.gayatri_blog.model.Blog;
import com.omnify.gayatri_blog.service.BlogService;
import com.omnify.gayatri_blog.util.BadRequestError;
import com.omnify.gayatri_blog.util.JwtUtil;
import com.omnify.gayatri_blog.util.UnauthorizedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/blogs")
public class BlogController {

    @Autowired
    BlogService bs;

    @Autowired
    JwtUtil j;

    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestHeader("Authorization") Optional<String> token, @RequestBody Blog b){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());

        if(b.getTitle() == null || b.getContent() == null){
            throw new BadRequestError("Please all the required fields");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(bs.createBlog(b, Id));
    }

    @GetMapping
    public ResponseEntity<Page<Blog>> getAllBlogs(Pageable p){
        return ResponseEntity.status(HttpStatus.OK).body(bs.getAllBlog(p));
    }

    @GetMapping("user")
    public ResponseEntity<List<Blog>> getUserBlogs(@RequestHeader("Authorization")  Optional<String> token){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());

        List<Blog> l = bs.getBlogByUser(Id);
        return ResponseEntity.status(HttpStatus.OK).body(l);
    }

    @GetMapping("{id}")
    public ResponseEntity<Blog> getBlogById(@RequestHeader("Authorization")  Optional<String> token, @PathVariable Long id){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());

        return ResponseEntity.status(HttpStatus.OK).body(bs.getBlogById(id, Id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Blog> updateBlogById(@RequestHeader("Authorization")  Optional<String> token, @RequestBody Blog b, @PathVariable Long id){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());
        b.setId(id);

        if(b.getTitle() == null || b.getContent() == null) {
            throw new BadRequestError("Please fill all the required fields");
        }

        Blog updatedBlog = bs.updateBlogById(b, Id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBlog);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBlogById(@RequestHeader("Authorization")  Optional<String> token, @PathVariable Long id){
        if (!token.isPresent() || token.isEmpty()) {
            throw new UnauthorizedError("Token not found");
        }
        Long Id = j.extractId(token.get());

        bs.deleteBlogById(id, Id);

        return ResponseEntity.status(HttpStatus.OK).body("Blog Deleted Successfully");
    }

}
