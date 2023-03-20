package com.ttokey.blog.feign;

import com.ttokey.blog.feign.dto.NaverSearchBlogReq;
import com.ttokey.blog.feign.dto.NaverSearchBlogRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverBlogSearchClient", url = "https://openapi.naver.com")
public interface NaverSearchBlogClient {
    @GetMapping(value = "/v1/search/blog.json")
    NaverSearchBlogRes blogSearch(
            @SpringQueryMap NaverSearchBlogReq naverSearchBlogReq,
            @RequestHeader("X-Naver-Client-Id") String clientId,
            @RequestHeader("X-Naver-Client-Secret") String clientSecret
    );

}
