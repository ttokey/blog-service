package com.ttokey.blog.repository;

import com.ttokey.blog.entity.SearchWordMst;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchWordMstRepository extends JpaRepository<SearchWordMst, Long> {
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    Optional<SearchWordMst> findOneByWord(String word);
}
