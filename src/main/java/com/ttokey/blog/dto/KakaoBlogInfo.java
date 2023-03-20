package com.ttokey.blog.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KakaoBlogInfo extends CommonBlogInfo {
    String thumbnail;

    @Builder
    public KakaoBlogInfo(String title, String contents, String url, String blogName, String dateTime, String thumbnail) {
        super(title, contents, url, blogName, dateTime);
        this.thumbnail = thumbnail;
    }
}
