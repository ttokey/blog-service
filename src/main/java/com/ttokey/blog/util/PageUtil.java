package com.ttokey.blog.util;

public final class PageUtil {
    public static int pageSize(int total, int size) {
        return (int) Math.ceil((double) total / size);
    }
}
