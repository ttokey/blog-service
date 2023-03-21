package com.ttokey.blog.component;

import com.ttokey.blog.repository.SearchWordHstRepository;
import com.ttokey.blog.repository.SearchWordMstRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class SearchWordComponentTest {
    @Autowired
    private SearchWordComponent searchWordComponent;

    @Autowired
    private SearchWordMstRepository searchWordMstRepository;
    @Autowired
    private SearchWordHstRepository searchWordHstRepository;

    @Test
    public void saveSearchWord() {
        //given
        String word = "카카오";

        //when

        searchWordComponent.saveSearchWord(word);


        //then
        Assertions.assertThat(searchWordMstRepository.findOneByWord(word)).isPresent();
        Assertions.assertThat(searchWordHstRepository.findAllByWord(word)).isNotEmpty();
        System.out.println("hi");
    }

    @Test
    public void testSearchWordWithTransaction() throws InterruptedException {
        //given
        String word = "카카오";
        int expect = 100;
        ExecutorService service = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);

        //when
        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                searchWordComponent.saveSearchWord(word);
                latch.countDown();
            });
        }
        latch.await();

        //then
        Assertions.assertThat(searchWordComponent.getCount(word)).isEqualTo(expect);
    }
}