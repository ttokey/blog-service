package com.ttokey.blog.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TtokeyErrorType {
    BLOG_SEARCH_FAIL("Blog search 실패"),
    ;

    String message;
    
}
