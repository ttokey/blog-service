package com.ttokey.blog.feign;

import com.ttokey.blog.enumeration.TtokeyErrorType;
import com.ttokey.blog.exception.TtokeyException;
import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.NaverSearchBlogRes;
import com.ttokey.blog.feign.dto.SearchBlog;
import com.ttokey.blog.property.SearchAuthorizationProperty;
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
        return kaKaoSearchBlogClient.blogSearch(kakaoSearchBlogReq, SearchAuthorizationProperty.KAKAO_AUTH);
    }

    NaverSearchBlogRes blogSearchFallback(KakaoSearchBlogReq kakaoSearchBlogReq, Throwable ex) {
        NaverSearchBlogRes naverSearchBlogRes;
        try {
            naverSearchBlogRes = naverSearchBlogClient.blogSearch(kakaoSearchBlogReq.toNaverSearchBlogReq()
                    , SearchAuthorizationProperty.NAVER_CLIENT_ID
                    , SearchAuthorizationProperty.NAVER_CLIENT_SECRET);
        } catch (Exception exception) {
            throw new TtokeyException(TtokeyErrorType.BLOG_SEARCH_FAIL);
        }
        return naverSearchBlogRes;
    }
}
