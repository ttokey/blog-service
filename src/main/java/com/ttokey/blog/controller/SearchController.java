package com.ttokey.blog.controller;

import com.ttokey.blog.constant.UrlConstant;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.dto.TopTenWordRes;
import com.ttokey.blog.enumeration.SortType;
import com.ttokey.blog.service.SearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = UrlConstant.VERSION + UrlConstant.SEARCH)
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final SearchService searchService;

    @GetMapping(value = UrlConstant.BLOG)
    public SearchBlogRes searchBlog(@RequestParam @NotEmpty(message = "query 는 비어있으면 안됩니다") String query
            , @RequestParam(value = "sort", defaultValue = "accuracy") String sortType
            , @RequestParam(defaultValue = "1") @Min(1) @Max(50) Integer page
            , @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer size) {
        SearchBlogReq searchBlogReq = SearchBlogReq.builder()
                .query(query)
                .sortType(SortType.of(sortType))
                .page(page)
                .size(size)
                .build();
        return searchService.blogSearch(searchBlogReq);
    }

    @GetMapping(value = UrlConstant.TOP_TEN_WORD)
    public TopTenWordRes getTopTenWord() {
        return searchService.getTopTenWord();
    }
}
