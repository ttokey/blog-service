package com.ttokey.blog.component;

import com.ttokey.blog.entity.SearchWordHst;
import com.ttokey.blog.entity.SearchWordMst;
import com.ttokey.blog.repository.SearchWordHstRepository;
import com.ttokey.blog.repository.SearchWordMstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchWordComponent {
    private final SearchWordMstRepository searchWordMstRepository;
    private final SearchWordHstRepository searchWordHstRepository;

    @Transactional
    @Retryable(retryFor = {SQLException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100L))
    public void saveSearchWord(String word) {
        SearchWordMst searchWordMst = searchWordMstRepository.findOneByWord(word)
                .orElseGet(() -> SearchWordMst.builder().word(word).build());
        searchWordMst.addCount();
        SearchWordHst searchWordHst = searchWordMst.toHistory();
        log.info("{} : {}", word, searchWordMst.getCount());
        searchWordHstRepository.save(searchWordHst);
        searchWordMstRepository.save(searchWordMst);
    }


    @Transactional
    public Long getCount(String word) {
        SearchWordMst searchWordMst = searchWordMstRepository.findOneByWord(word)
                .orElseGet(() -> SearchWordMst.builder().word(word).build());
        return searchWordMst.getCount();
    }
}
