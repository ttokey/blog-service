package com.ttokey.blog.property.service;

import com.ttokey.blog.component.SearchWordComponent;
import com.ttokey.blog.dto.BlogInfo;
import com.ttokey.blog.dto.PageInfo;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.enumeration.BlogType;
import com.ttokey.blog.enumeration.SortType;
import com.ttokey.blog.feign.SearchBlogClient;
import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.KakaoSearchBlogRes;
import com.ttokey.blog.service.SearchService;
import com.ttokey.blog.util.PageUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    SearchBlogClient searchBlogClient;
    @Mock
    SearchWordComponent searchWordComponent;
    @InjectMocks
    SearchService searchService;

    @Test
    public void blogSearch() {
        //given
        int totalCount = 999;
        int pageableCount = 436;
        boolean isEnd = false;
        String title1 = "본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션";
        String contents1 = "판매 수익금 기부, <b>카카오</b>톡 채널 추가 쿠폰 증정 \u200B https://naver.me/G5QhNN4b 본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션 본죽이 브랜드 모델 임영웅과 함께 한 광고를 공개하고 2주간 할인 행사를 진행한다고 20일 밝혔다. 내달 2일까지 배달앱 배달의민족에서 본죽 전 메뉴 구매 시 일일 선착순 4000명에게 최대...";
        String url1 = "https://blog.naver.com/ohmiy0727/223050498189";
        String blogName1 = "재미있는 이야기";
        String datetime1 = "2023-03-20T21:00:26.000+09:00";
        String thumbnail1 = "";
        String title2 = "[강남역] 에스프레소바 파라볼레 PARABOLE";
        String contents2 = "커다란 창이 나있어서 분위기가 좋다";
        String url2 = "http://starbongja.tistory.com/19";
        String blogName2 = "스타봉자";
        String datetime2 = "2023-03-20T21:00:15.000+09:00";
        String thumbnail2 = "https://search1.kakaocdn.net/argon/130x130_85_c/H26sb0LbQF1";
        SortType sortType = SortType.ACCURACY;
        int size = 2;
        int page = 10;
        String query = "카카오";
        KakaoSearchBlogRes.Meta meta = KakaoSearchBlogRes.Meta.builder()
                .totalCount(totalCount)
                .pageableCount(pageableCount)
                .isEnd(isEnd)
                .build();

        List<KakaoSearchBlogRes.Document> documents = new ArrayList<>();
        documents.add(KakaoSearchBlogRes.Document.builder()
                .title(title1)
                .contents(contents1)
                .url(url1)
                .blogName(blogName1)
                .dateTime(datetime1)
                .thumbnail(thumbnail1)
                .build());
        documents.add(KakaoSearchBlogRes.Document.builder()
                .title(title2)
                .contents(contents2)
                .url(url2)
                .blogName(blogName2)
                .dateTime(datetime2)
                .thumbnail(thumbnail2)
                .build());
        KakaoSearchBlogRes kakaoSearchBlogRes = KakaoSearchBlogRes.builder()
                .meta(meta)
                .documents(documents)
                .build();
        SearchBlogReq searchBlogReq = SearchBlogReq.builder()
                .page(page)
                .size(size)
                .sortType(sortType)
                .query(query)
                .build();

        PageInfo pageInfo = PageInfo.builder()
                .sortType(sortType)
                .pageSize(PageUtil.pageSize(pageableCount, size))
                .pageNumber(page)
                .totalCount(pageableCount)
                .size(2)
                .isEnd(isEnd)
                .build();
        List<BlogInfo> blogInfoList = new ArrayList<>();

        blogInfoList.add(BlogInfo.builder()
                .title(title1)
                .contents(contents1)
                .url(url1)
                .blogName(blogName1)
                .thumbnail(thumbnail1)
                .dateTime(datetime1)
                .build());

        blogInfoList.add(BlogInfo.builder()
                .title(title2)
                .contents(contents2)
                .url(url2)
                .blogName(blogName2)
                .thumbnail(thumbnail2)
                .dateTime(datetime2)
                .build());

        SearchBlogRes expect = SearchBlogRes.builder()
                .blogType(BlogType.KAKAO)
                .pageInfo(pageInfo)
                .blogInfos(blogInfoList)
                .build();

        when(searchBlogClient.searchBlog(any(KakaoSearchBlogReq.class))).thenReturn(kakaoSearchBlogRes);

        //when
        SearchBlogRes result = searchService.blogSearch(searchBlogReq);

        //then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }
}