package com.ttokey.blog.feign.dto;

import com.ttokey.blog.enumeration.SortType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NaverSearchBlogReq {
    @NotEmpty
    String query;
    String sort;
    Integer start;
    Integer display;

    @Builder
    public NaverSearchBlogReq(KakaoSearchBlogReq kakaoSearchBlogReq) {
        this.query = kakaoSearchBlogReq.getQuery();
        this.sort = SortType.of(kakaoSearchBlogReq.getSort()).getNaverType();
        this.start = (kakaoSearchBlogReq.getPage() - 1) * kakaoSearchBlogReq.getSize() + 1;
        this.display = kakaoSearchBlogReq.getSize();
    }
}
