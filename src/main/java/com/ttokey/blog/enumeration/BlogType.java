package com.ttokey.blog.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogType {
    KAKAO("kakao"),
    NAVER("naver"),
    ;

    private String name;

    @JsonValue
    public String getName() {
        return this.name;
    }


}
