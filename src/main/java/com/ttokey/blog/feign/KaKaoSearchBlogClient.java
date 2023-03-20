package com.ttokey.blog.feign;

import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.KakaoSearchBlogRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoBlogSearchClient", url = "${search.blog.kakao-url}")
public interface KaKaoSearchBlogClient {
    @GetMapping(value = "${search.blog.kakao-path}")
    KakaoSearchBlogRes blogSearch(
            @SpringQueryMap KakaoSearchBlogReq kakaoSearchBlogReq,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}
