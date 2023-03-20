package com.ttokey.blog.dto;

import com.ttokey.blog.enumeration.SortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PageInfo {
    SortType sortType;
    Integer pageSize;
    Integer pageNumber;
    Integer totalCount;
    Integer size;
    Boolean isEnd;
}
