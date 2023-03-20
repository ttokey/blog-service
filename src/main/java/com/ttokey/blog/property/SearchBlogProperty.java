package com.ttokey.blog.property;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "search.blog")
public final class SearchBlogProperty {
    public static String KAKAO_URL;
    public static String KAKAO_PATH;
    public static String NAVER_URL;
    public static String NAVER_PATH;

    public void setKakaoUrl(String kakaoUrl) {
        KAKAO_URL = kakaoUrl;
    }

    public void setKakaoPath(String kakaoPath) {
        KAKAO_PATH = kakaoPath;
    }

    public void setNaverUrl(String naverUrl) {
        NAVER_URL = naverUrl;
    }

    public void setNaverPath(String naverPath) {
        NAVER_PATH = naverPath;
    }
}
