package com.ttokey.blog.component;

import com.ttokey.blog.entity.SearchWordHst;
import com.ttokey.blog.entity.SearchWordMst;
import com.ttokey.blog.repository.SearchWordHstRepository;
import com.ttokey.blog.repository.SearchWordMstRepository;
import com.ttokey.blog.repository.SearchWordTopTenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchWordComponent {
    private final SearchWordMstRepository searchWordMstRepository;
    private final SearchWordHstRepository searchWordHstRepository;
    private final SearchWordTopTenRepository searchWordTopTenRepository;

    @Transactional
    @Retryable(retryFor = {SQLException.class}, maxAttempts = 2, backoff = @Backoff(delay = 100L))
    public void saveSearchWord(String word) {
        SearchWordMst searchWordMst = searchWordMstRepository.findOneByWord(word)
                .orElseGet(() -> SearchWordMst.builder().word(word).build());
        searchWordMst.addCount();
        SearchWordHst searchWordHst = searchWordMst.toHistory();
        searchWordHstRepository.save(searchWordHst);
        SearchWordMst result = searchWordMstRepository.save(searchWordMst);
        manageTopTen(result);
    }

    public void manageTopTen(SearchWordMst input) {
        List<SearchWordMst> topTenList = getTopTen();
        if (topTenList.stream().anyMatch(mst -> Objects.equals(mst.getId(), input.getId()))) {
            return;
        }
        if (topTenList.isEmpty() || topTenList.size() < 10) {
            searchWordTopTenRepository.save(input.toTopTen());
            return;
        }
        if (topTenList.get(0).getCount() <= input.getCount()) {
            searchWordTopTenRepository.deleteById(topTenList.get(0).getId());
            searchWordTopTenRepository.save(input.toTopTen());
        }
    }

    public List<SearchWordMst> getTopTen() {
        return searchWordTopTenRepository.findAll().stream()
                .map(searchWordTopTen -> searchWordMstRepository.findById(searchWordTopTen.getId()).get())
                .sorted(this::compareSearchWordMst)
                .collect(Collectors.toList());
    }

    private int compareSearchWordMst(SearchWordMst a, SearchWordMst b) {
        if (a.getCount() < b.getCount()) {
            return -1;
        } else if (Objects.equals(a.getCount(), b.getCount())) {
            return 0;
        }
        return 1;
    }


    @Transactional
    public Long getCount(String word) {
        SearchWordMst searchWordMst = searchWordMstRepository.findOneByWord(word)
                .orElseGet(() -> SearchWordMst.builder().word(word).build());
        return searchWordMst.getCount();
    }
}
