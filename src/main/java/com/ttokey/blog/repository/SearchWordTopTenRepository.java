package com.ttokey.blog.repository;

import com.ttokey.blog.entity.SearchWordTopTen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchWordTopTenRepository extends JpaRepository<SearchWordTopTen, Long> {

}
