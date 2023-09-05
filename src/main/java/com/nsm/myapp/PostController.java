package com.nsm.myapp;

import com.nsm.myapp.entity.Post;
import com.nsm.myapp.repository.PostRepository;
import com.nsm.myapp.request.PostModifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Tag(name ="애견 커뮤니티")
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    @Autowired
    PostRepository repo;



    @GetMapping
    public List<Post> getPostList(@RequestParam String post) {
        System.out.println(post + "-------------------------------------");
//        List<Post> list = repo.findPostSortByNo();
        List<Post> list = repo.findByBoardValue(post);
        return list;

    }

    @GetMapping(value = "/paging")
    @Operation(summary = "게시물페이징")
    public Page<Post> getPostsPaging(@RequestParam String post, @RequestParam String creatorName, @RequestParam int page, @RequestParam int size) {
        System.out.println(page + "1");
        System.out.println(size + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByBoardValueAndCreatorName(post,creatorName,pageRequest);
    }

    @PostMapping(value = "/addPost")
    @Operation(summary = "게시물작성")
    public ResponseEntity<Map<String, Object>> addPost(@RequestBody Post post) {
        System.out.println(post + "-------------------------------------------------");
        post.setCreatorName("매니저");

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


    @DeleteMapping(value = "/removePost")
    @Operation(summary = "게시물삭제")
    public ResponseEntity removePost(@RequestParam long no) {
        System.out.println(no + "---------------------------");

        Optional<Post> post = repo.findPostByNo(no);

        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (post.get().getNo() != no) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/modifyPost")
    @Operation(summary = "게시물수정")
    public ResponseEntity modifyPost(@RequestParam long no, @RequestBody PostModifyRequest post) {
        System.out.println(no);
        System.out.println(post);

        Optional<Post> findedPost = repo.findById(no);
        if (!findedPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 조회된게시물없으면 404반환
        }
        Post toModifyPost = findedPost.get();   // 조회 한 게시물 수정으로 설정

        if (post.getTitle() != null && !post.getTitle().isEmpty()) {
            toModifyPost.setTitle(post.getTitle());
        }
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            toModifyPost.setContent(post.getContent());
        }
        // 업데이트
        repo.save(toModifyPost);

        // OK 처리
        return ResponseEntity.ok().build();
    }


}