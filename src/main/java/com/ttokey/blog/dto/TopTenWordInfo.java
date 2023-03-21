package com.ttokey.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TopTenWordInfo {
    String word;
    Long count;
}
