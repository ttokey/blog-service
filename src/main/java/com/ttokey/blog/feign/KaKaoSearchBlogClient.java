package com.ttokey.blog.feign;

import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.KakaoSearchBlogRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoBlogSearchClient", url = "https://dapi.kakao.com")
public interface KaKaoSearchBlogClient {
    @GetMapping(value = "/v2/search/blog")
    KakaoSearchBlogRes blogSearch(
            @SpringQueryMap KakaoSearchBlogReq kakaoSearchBlogReq,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}
