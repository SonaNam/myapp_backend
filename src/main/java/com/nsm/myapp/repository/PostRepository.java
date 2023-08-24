package com.nsm.myapp.repository;

import com.nsm.myapp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository <Post,Long>{
    @Query(value = "select * from post order by no asc", nativeQuery = true)
        // 모든 게시물을 가져와서 게시물 번호(no)를 오름차순으로정렬
    List<Post> findPostSortByNo();
    @Query(value = "select * from post where no = :no", nativeQuery = true)
        //게시물조회 , 게시물을 옵셔널로 감싸서반환 ,존재하지않으면 .empty() 반환
    Optional<Post> findPostByNo(Long no);
    //작성자이름에 특정문자열을 포함하는 게시물들을 페이징된 형태로 조회반환
//    Page<Post> findByCreatorNameContains(String creatorName, Pageable pageable);

//    주어진 게시물 번호(no)와 페이징정보(pageable)를이용해 해당번호의 게시물을 조회반환
//    Page<Post> findByNo(long no, Pageable pageable);

//   작성자ID와 페이징정보를이용하여 해당 작성자의게시물을 조회반환
//    Page<Post> findByCreatorId(long creatorId, Pageable pageable);
//    작성자 이름 또는 게시물 내용에 특정 문자열을 포함하는 게시물들을 페이징된 형태로 조회하여 반환합니다.
//    작성자 이름과 게시물 내용 중 어느 하나라도 검색 조건을 만족하면 결과에
//    Page<Post> findByCreatorNameContainsOrContentContains(String creatorName, String content, Pageable pageable);
}
