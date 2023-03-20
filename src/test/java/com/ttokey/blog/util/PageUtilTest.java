package com.ttokey.blog.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PageUtilTest {
    @Test
    public void pageSize() {
        //given
        int[] totalArray = {15, 9, 7, 20, 99};
        int[] sizeArray = {3, 5, 1, 9, 10};
        int[] expect = {5, 2, 7, 3, 10};

        //when

        //then
        for (int i = 0; i < totalArray.length; i++) {
            Assertions.assertThat(PageUtil.pageSize(totalArray[i], sizeArray[i])).isEqualTo(expect[i]);
        }
    }

}