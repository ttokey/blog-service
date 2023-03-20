package com.ttokey.blog.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SortType {
    ACCURACY("accuracy", "sim"),
    RECENCY("recency", "date"),
    ;

    String kakaoType;
    String naverType;

    private static final Map<String, SortType> lookUp = EnumSet.allOf(SortType.class).stream()
            .collect(Collectors.toMap(SortType::getKakaoType, p -> p));

    @JsonCreator
    public static SortType of(String type) {
        return lookUp.getOrDefault(type, ACCURACY);
    }

    @JsonValue
    public String getKakaoType() {
        return kakaoType;
    }
}
