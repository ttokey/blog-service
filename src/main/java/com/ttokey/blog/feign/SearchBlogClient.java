package com.ttokey.blog.feign;

import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.NaverSearchBlogRes;
import com.ttokey.blog.feign.dto.SearchBlog;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchBlogClient {
    private final KaKaoSearchBlogClient kaKaoSearchBlogClient;
    private final NaverSearchBlogClient naverSearchBlogClient;

    @CircuitBreaker(name = "kakaoBlogSearchClient_searchBlog", fallbackMethod = "blogSearchFallback")
    public SearchBlog searchBlog(KakaoSearchBlogReq kakaoSearchBlogReq) {
        return kaKaoSearchBlogClient.blogSearch(kakaoSearchBlogReq, "KakaoAKcfb766ea794157a822528c71cd38ce0b");
    }

    NaverSearchBlogRes blogSearchFallback(KakaoSearchBlogReq kakaoSearchBlogReq, Throwable ex) {
        // call the fallback method here, e.g.
        return naverSearchBlogClient.blogSearch(kakaoSearchBlogReq.toNaverSearchBlogReq(), "UmSwQxTOfWrSkmxm9fUt", "f6kNJnt1ft");
    }
}
