package com.ttokey.blog.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttokey.blog.dto.BlogInfo;
import com.ttokey.blog.dto.PageInfo;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.enumeration.BlogType;
import com.ttokey.blog.util.PageUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NaverSearchBlogRes implements SearchBlog {
    String lastBuildDate;
    Integer total;
    Integer start;
    Integer display;
    List<Item> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Item {
        String title;
        String description;
        String link;
        @JsonProperty("bloggername")
        String bloggerName;
        String postdate;
        @JsonProperty("bloggerlink")
        String bloggerLink;

        public BlogInfo toBlogInfo() {
            return BlogInfo.builder()
                    .title(title)
                    .contents(description)
                    .url(link)
                    .blogName(bloggerName)
                    .dateTime(postdate)
                    .bloggerLink(bloggerLink)
                    .build();
        }
    }

    @Override
    public SearchBlogRes toSearchBlogRes(SearchBlogReq searchBlogReq) {
        int pageSize = PageUtil.pageSize(this.total, searchBlogReq.getSize());
        Integer page = searchBlogReq.getPage();
        PageInfo pageInfo = PageInfo.builder()
                .sortType(searchBlogReq.getSortType())
                .pageSize(pageSize)
                .pageNumber(page)
                .totalCount(this.total)
                .size(this.items.size())
                .isEnd(page >= pageSize)
                .build();
        return SearchBlogRes.builder()
                .blogType(BlogType.NAVER)
                .pageInfo(pageInfo)
                .blogInfos(toBlogInfoList())
                .build();
    }


    private List<BlogInfo> toBlogInfoList() {
        return this.items.stream()
                .map(item -> item.toBlogInfo())
                .collect(Collectors.toList());
    }


}
