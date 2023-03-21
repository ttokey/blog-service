package com.ttokey.blog.service;

import com.ttokey.blog.component.SearchWordComponent;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.feign.SearchBlogClient;
import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.SearchBlog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchBlogClient searchBlogClient;
    private final SearchWordComponent searchWordComponent;

    public SearchBlogRes blogSearch(SearchBlogReq searchBlogReq) {
        KakaoSearchBlogReq kakaoSearchBlogReq = KakaoSearchBlogReq.builder()
                .searchBlogReq(searchBlogReq)
                .build();
        SearchBlog searchBlog = searchBlogClient.searchBlog(kakaoSearchBlogReq);
        return searchBlog.toSearchBlogRes(searchBlogReq);
    }

    public void searchWord(String word) {
        searchWordComponent.saveSearchWord(word);
    }
}