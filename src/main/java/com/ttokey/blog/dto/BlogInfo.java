package com.ttokey.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class BlogInfo {
    String title;
    String contents;
    String url;
    String blogName;
    String dateTime;
    String thumbnail;
    String bloggerLink;
}
