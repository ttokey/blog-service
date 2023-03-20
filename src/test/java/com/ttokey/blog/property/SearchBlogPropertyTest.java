package com.ttokey.blog.property;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "search.blog.kakao-url=test-kakao-url",
        "search.blog.kakao-path=test-kakao-path",
        "search.blog.naver-url=test-naver-url",
        "search.blog.naver-path=test-naver-path"
})
class SearchBlogPropertyTest {
    @Test
    public void KAKAO_URL() {
        //given
        String expect = "test-kakao-url";
        //when

        //then
        Assertions.assertThat(SearchBlogProperty.KAKAO_URL).isEqualTo(expect);
    }

    @Test
    public void KAKAO_PATH() {
        //given
        String expect = "test-kakao-path";
        //when

        //then
        Assertions.assertThat(SearchBlogProperty.KAKAO_PATH).isEqualTo(expect);
    }

    @Test
    public void NAVER_URL() {
        //given
        String expect = "test-naver-url";
        //when

        //then
        Assertions.assertThat(SearchBlogProperty.NAVER_URL).isEqualTo(expect);
    }

    @Test
    public void NAVER_PATH() {
        //given
        String expect = "test-naver-path";
        //when

        //then
        Assertions.assertThat(SearchBlogProperty.NAVER_PATH).isEqualTo(expect);
    }
}