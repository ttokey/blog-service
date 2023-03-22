package com.ttokey.blog.component;

import com.ttokey.blog.entity.SearchWordMst;
import com.ttokey.blog.entity.SearchWordTopTen;
import com.ttokey.blog.repository.SearchWordHstRepository;
import com.ttokey.blog.repository.SearchWordMstRepository;
import com.ttokey.blog.repository.SearchWordTopTenRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class SearchWordComponentTest {
    @Autowired
    private SearchWordComponent searchWordComponent;


    @Autowired
    private SearchWordMstRepository searchWordMstRepository;
    @Autowired
    private SearchWordHstRepository searchWordHstRepository;
    @Autowired
    private SearchWordTopTenRepository searchWordTopTenRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void beforeEach() {
        searchWordMstRepository.deleteAll();
        searchWordTopTenRepository.deleteAll();
        searchWordHstRepository.deleteAll();
        em.flush();
    }

    @Test
    public void saveSearchWord() {
        //given
        String word = "카카오";

        //when

        searchWordComponent.saveSearchWord(word);


        //then
        Assertions.assertThat(searchWordMstRepository.findOneByWord(word)).isPresent();
        Assertions.assertThat(searchWordHstRepository.findAllByWord(word)).isNotEmpty();
    }

    @Test
    @DisplayName("top ten table이 10개 미만인 경우")
    public void testOrderBy_under10() {
        //given

        List<SearchWordTopTen> expect = new ArrayList<>();
        List<String> wordList = Arrays.asList("카카오", "네이버", "이석희", "동시성", "제어");
        List<Integer> countList = Arrays.asList(99, 13, 1000, 5, 1);
        for (int i = 0; i < wordList.size(); i++) {
            SearchWordMst searchWordMst = SearchWordMst.builder().word(wordList.get(i)).build();
            for (int j = 0; j < countList.get(i); j++) {
                searchWordMst.addCount();
            }
            SearchWordMst result = searchWordMstRepository.save(searchWordMst);
            searchWordComponent.manageTopTen(result);
            expect.add(searchWordMst.toTopTen());
        }

        //when
        List<SearchWordTopTen> result = searchWordTopTenRepository.findAll();


        //then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("top ten table이 10개 이상인 경우")
    public void testOrderBy_over10() {
        //given

        List<SearchWordTopTen> expect = new ArrayList<>();
        List<String> wordList = Arrays.asList("카카오", "네이버", "이석희", "동시성", "제어", "테스트문구1", "테스트2", "테스트3", "테스트4", "테스트5", "테스트6");
        List<Integer> countList = Arrays.asList(99, 13, 1000, 91, 15, 9, 5, 17, 20, 23, 10);
        for (int i = 0; i < wordList.size(); i++) {
            SearchWordMst searchWordMst = SearchWordMst.builder().word(wordList.get(i)).build();
            for (int j = 0; j < countList.get(i); j++) {
                searchWordMst.addCount();
            }
            SearchWordMst result = searchWordMstRepository.save(searchWordMst);
            searchWordComponent.manageTopTen(result);
            expect.add(searchWordMst.toTopTen());
        }
        expect.remove(6);


        //when
        List<SearchWordTopTen> result = searchWordTopTenRepository.findAll();


        //then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expect);
    }

    @Test
    public void getTopTen() {
        //given

        List<SearchWordTopTen> searchWordTopTenList = new ArrayList<>();
        List<String> wordList = Arrays.asList("카카오", "네이버", "이석희", "동시성", "제어", "테스트문구1", "테스트2", "테스트3", "테스트4", "테스트5");
        List<Integer> countList = Arrays.asList(99, 13, 1000, 91, 15, 9, 5, 17, 20, 23);
        for (int i = 0; i < wordList.size(); i++) {
            SearchWordMst searchWordMst = SearchWordMst.builder().word(wordList.get(i)).build();
            for (int j = 0; j < countList.get(i); j++) {
                searchWordMst.addCount();
            }
            searchWordMstRepository.save(searchWordMst);
            searchWordTopTenRepository.save(SearchWordTopTen.builder().id(searchWordMst.getId()).build());
            searchWordTopTenList.add(searchWordMst.toTopTen());
        }
        List<SearchWordTopTen> expect = new ArrayList<>();
        expect.add(searchWordTopTenList.get(2));
        expect.add(searchWordTopTenList.get(0));
        expect.add(searchWordTopTenList.get(3));
        expect.add(searchWordTopTenList.get(9));
        expect.add(searchWordTopTenList.get(8));
        expect.add(searchWordTopTenList.get(7));
        expect.add(searchWordTopTenList.get(4));
        expect.add(searchWordTopTenList.get(1));
        expect.add(searchWordTopTenList.get(5));
        expect.add(searchWordTopTenList.get(6));


        //when
        List<SearchWordTopTen> result = searchWordTopTenRepository.findAll();


        //then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(searchWordTopTenList);
    }

    @Test
    public void getTopTen_concurrency_one_thread() throws InterruptedException {
        //given
        List<String> wordList = Arrays.asList("카카오", "네이버", "이석희", "동시성", "제어", "테스트문구1", "테스트2", "테스트3", "테스트4", "테스트5", "테스트6");
        List<Integer> countList = Arrays.asList(99, 13, 1000, 91, 15, 9, 5, 17, 20, 23, 10);
        List<SearchWordMst> expect = new ArrayList<>();
        int[] expectIndexArray = {2, 0, 3, 9, 8, 7, 4, 1, 10, 5};
        for (int expectIndex : expectIndexArray) {
            expect.add(makeSearchWordMst(wordList.get(expectIndex), countList.get(expectIndex)));
        }


        //when
        for (int i = 0; i < wordList.size(); i++) {
            for (int j = 0; j < countList.get(i); j++) {
                final int finalI = i;
                searchWordComponent.saveSearchWord(wordList.get(finalI));
            }
        }
        List<SearchWordMst> result = searchWordComponent.getTopTen();

        //then
        Assertions.assertThat(result).usingRecursiveComparison().ignoringFields("id").ignoringFieldsOfTypes(LocalDateTime.class).isEqualTo(expect);
    }

    @Test
    public void getTopTen_concurrency_many_thread() throws InterruptedException {
        //given
        List<String> wordList = Arrays.asList("카카오", "네이버", "이석희", "동시성", "제어", "테스트문구1", "테스트2", "테스트3", "테스트4", "테스트5", "테스트6");
        List<Integer> countList = Arrays.asList(99, 13, 200, 91, 15, 9, 5, 17, 20, 23, 10);
        ExecutorService service = Executors.newFixedThreadPool(100);

        CountDownLatch latch = new CountDownLatch(502);
        List<SearchWordMst> expect = new ArrayList<>();
        int[] expectIndexArray = {2, 0, 3, 9, 8, 7, 4, 1, 10, 5};
        for (int expectIndex : expectIndexArray) {
            expect.add(makeSearchWordMst(wordList.get(expectIndex), countList.get(expectIndex)));
        }


        //when
        for (int i = 0; i < wordList.size(); i++) {
            for (int j = 0; j < countList.get(i); j++) {
                final int finalI = i;
                service.execute(() -> {
                    searchWordComponent.saveSearchWord(wordList.get(finalI));
                    latch.countDown();
                });
            }
        }
        latch.await();

        List<SearchWordMst> result = searchWordComponent.getTopTen();

        //then
        Assertions.assertThat(result).usingRecursiveComparison().ignoringFields("id").ignoringFieldsOfTypes(LocalDateTime.class).isEqualTo(expect);
        service.shutdown();
    }

    private SearchWordMst makeSearchWordMst(String word, int count) {
        SearchWordMst searchWordMst = SearchWordMst.builder().word(word).build();
        for (int i = 0; i < count; i++) {
            searchWordMst.addCount();
        }
        return searchWordMst;
    }
}