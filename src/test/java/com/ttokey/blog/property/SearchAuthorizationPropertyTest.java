package com.ttokey.blog.property;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "search.authorization.kakao-auth=test-kakao-auth",
        "search.authorization.naver-client-id=test-naver-client-id",
        "search.authorization.naver-client-secret=test-naver-client-secret"
})
class SearchAuthorizationPropertyTest {
    @Test
    public void KAKAO_AUTH() {
        //given
        String expect = "test-kakao-auth";
        //when

        //then
        Assertions.assertThat(SearchAuthorizationProperty.KAKAO_AUTH).isEqualTo(expect);
    }

    @Test
    public void NAVER_CLIENT_ID() {
        //given
        String expect = "test-naver-client-id";
        //when

        //then
        Assertions.assertThat(SearchAuthorizationProperty.NAVER_CLIENT_ID).isEqualTo(expect);
    }

    @Test
    public void NAVER_CLIENT_SECRET() {
        //given
        String expect = "test-naver-client-secret";
        //when

        //then
        Assertions.assertThat(SearchAuthorizationProperty.NAVER_CLIENT_SECRET).isEqualTo(expect);
    }
}