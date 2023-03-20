package com.ttokey.blog.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommonBlogInfo {
    String title;
    String contents;
    String url;
    String blogName;
    String dateTime;
}
