package com.ttokey.blog.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "search.authorization")
public final class SearchAuthorizationProperty {
    public static String KAKAO_AUTH;
    public static String NAVER_CLIENT_SECRET;
    public static String NAVER_CLIENT_ID;

    public void setKakaoAuth(String kakaoAuth) {
        KAKAO_AUTH = kakaoAuth;
    }

    public void setNaverClientId(String naverClientId) {
        NAVER_CLIENT_ID = naverClientId;
    }

    public void setNaverClientSecret(String naverClientSecret) {
        NAVER_CLIENT_SECRET = naverClientSecret;
    }
}
