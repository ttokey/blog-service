package com.ttokey.blog.repository;

import com.ttokey.blog.entity.SearchWordHst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchWordHstRepository extends JpaRepository<SearchWordHst, Long> {
    List<SearchWordHst> findAllByWord(String word);
}
