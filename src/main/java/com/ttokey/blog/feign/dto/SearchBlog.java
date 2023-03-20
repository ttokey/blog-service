package com.ttokey.blog.feign.dto;

import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;

public interface SearchBlog {
    public SearchBlogRes toSearchBlogRes(SearchBlogReq searchBlogReq);
}
