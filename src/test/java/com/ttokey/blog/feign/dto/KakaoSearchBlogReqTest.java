package com.ttokey.blog.feign.dto;

import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.enumeration.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class KakaoSearchBlogReqTest {
    @Test
    public void toNaverSearchBlogReq() {
        //given
        String query = "카카오";
        SortType sortType = SortType.ACCURACY;
        int size = 10;
        int page = 3;
        SearchBlogReq searchBlogReq = SearchBlogReq.builder()
                .query(query)
                .sortType(sortType)
                .size(size)
                .page(page)
                .build();
        KakaoSearchBlogReq kakaoSearchBlogReq = KakaoSearchBlogReq.builder()
                .searchBlogReq(searchBlogReq)
                .build();

        //when
        NaverSearchBlogReq result = kakaoSearchBlogReq.toNaverSearchBlogReq();

        //then
        Assertions.assertThat(result.getDisplay()).isEqualTo(size);
        Assertions.assertThat(result.getQuery()).isEqualTo(query);
        Assertions.assertThat(result.getStart()).isEqualTo(21);
        Assertions.assertThat(result.getSort()).isEqualTo(sortType.getNaverType());
    }
}