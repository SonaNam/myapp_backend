package com.nsm.myapp;

import com.nsm.myapp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.PostRepository;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/posts")

public class PostController {
    @Autowired
    PostRepository repo;


    @GetMapping
    public List<Post> getPostList() {


        List<Post> list = repo.findPostSortByNo();
        return list;

    }
    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size) {
        System.out.println(page + "1");
        System.out.println(size + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post){

        if (post.getTitle() == null || post.getContent() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        post.setCreatedTime(new Date().getTime());
        Post savedPost = repo.save(post);

        if (savedPost != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("data", savedPost);
            res.put("message", "created");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }


        return ResponseEntity.ok().build(); //
    }
    }