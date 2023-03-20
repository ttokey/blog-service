package com.ttokey.blog.feign.dto;

import com.ttokey.blog.dto.SearchBlogReq;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KakaoSearchBlogReq {
    @NotEmpty
    String query;
    String sort;
    Integer page;
    Integer size;

    @Builder
    public KakaoSearchBlogReq(SearchBlogReq searchBlogReq) {
        this.query = searchBlogReq.getQuery();
        this.sort = searchBlogReq.getSortType().getKakaoType();
        this.page = searchBlogReq.getPage();
        this.size = searchBlogReq.getSize();
    }

    public NaverSearchBlogReq toNaverSearchBlogReq() {
        return NaverSearchBlogReq.builder()
                .kakaoSearchBlogReq(this)
                .build();
    }
}
