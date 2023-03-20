package com.ttokey.blog.dto;

import com.ttokey.blog.enumeration.SortType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class SearchBlogReq {
    @NotEmpty
    String query;
    SortType sortType;
    Integer page;
    Integer size;
}
