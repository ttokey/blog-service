package com.ttokey.blog.dto;

import com.ttokey.blog.enumeration.BlogType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class SearchBlogRes {
    BlogType blogType;
    PageInfo pageInfo;
    List<BlogInfo> blogInfos;
}
