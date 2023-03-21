package com.ttokey.blog.service;

import com.ttokey.blog.component.SearchWordComponent;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.dto.TopTenWordRes;
import com.ttokey.blog.entity.SearchWordMst;
import com.ttokey.blog.feign.SearchBlogClient;
import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.SearchBlog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        searchWordComponent.saveSearchWord(searchBlogReq.getQuery());
        return searchBlog.toSearchBlogRes(searchBlogReq);
    }

    public TopTenWordRes getTopTenWord() {
        List<SearchWordMst> searchWordMstList = searchWordComponent.getTopTen();
        return TopTenWordRes.builder()
                .topTenWordInfos(searchWordMstList.stream()
                        .map(SearchWordMst::toTopTenWordInfo)
                        .collect(Collectors.toList()))
                .build();
    }
}