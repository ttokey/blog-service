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
public class KakaoSearchBlogRes implements SearchBlog {
    Meta meta;
    List<Document> documents;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Meta {
        @JsonProperty(value = "total_count")
        Integer totalCount;
        @JsonProperty(value = "pageable_count")
        Integer pageableCount;
        @JsonProperty(value = "is_end")
        Boolean isEnd;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Document {
        String title;
        String contents;
        String url;
        @JsonProperty("blogname")
        String blogName;
        @JsonProperty("datetime")
        String dateTime;
        String thumbnail;

        public BlogInfo toBlogInfo() {
            return BlogInfo.builder()
                    .title(title)
                    .contents(contents)
                    .url(url)
                    .blogName(blogName)
                    .thumbnail(thumbnail)
                    .dateTime(dateTime)
                    .build();
        }
    }

    @Override
    public SearchBlogRes toSearchBlogRes(SearchBlogReq searchBlogReq) {
        int pageSize = PageUtil.pageSize(this.meta.getPageableCount(), searchBlogReq.getSize());
        PageInfo pageInfo = PageInfo.builder()
                .sortType(searchBlogReq.getSortType())
                .pageSize(pageSize)
                .pageNumber(searchBlogReq.getPage())
                .totalCount(this.meta.getPageableCount())
                .size(this.documents.size())
                .isEnd(this.meta.getIsEnd())
                .build();
        return SearchBlogRes.builder()
                .blogType(BlogType.KAKAO)
                .pageInfo(pageInfo)
                .blogInfos(toBlogInfoList())
                .build();
    }


    private List<BlogInfo> toBlogInfoList() {
        return this.documents.stream()
                .map(document -> document.toBlogInfo())
                .collect(Collectors.toList());
    }


}
