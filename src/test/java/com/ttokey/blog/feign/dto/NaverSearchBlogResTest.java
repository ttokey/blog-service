package com.ttokey.blog.feign.dto;

import com.ttokey.blog.dto.BlogInfo;
import com.ttokey.blog.dto.PageInfo;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.enumeration.BlogType;
import com.ttokey.blog.enumeration.SortType;
import com.ttokey.blog.util.PageUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NaverSearchBlogResTest {
    @Test
    public void toSearchBlogRes() {
        //given
        SortType sortType = SortType.ACCURACY;
        int size = 2;
        int page = 10;
        String query = "카카오";

        String title1 = "본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션";
        String description1 = "판매 수익금 기부, <b>카카오</b>톡 채널 추가 쿠폰 증정 \u200B https://naver.me/G5QhNN4b 본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션 본죽이 브랜드 모델 임영웅과 함께 한 광고를 공개하고 2주간 할인 행사를 진행한다고 20일 밝혔다. 내달 2일까지 배달앱 배달의민족에서 본죽 전 메뉴 구매 시 일일 선착순 4000명에게 최대...";
        String link1 = "https://blog.naver.com/ohmiy0727/223050498189";
        String bloggerName1 = "재미있는 이야기";
        String postdate1 = "2023-03-20T21:00:26.000+09:00";
        String bloggerLink1 = "blog.naver.com/hdk_6208";

        String title2 = "[강남역] 에스프레소바 파라볼레 PARABOLE";
        String description2 = "커다란 창이 나있어서 분위기가 좋다";
        String link2 = "http://starbongja.tistory.com/19";
        String bloggerName2 = "스타봉자";
        String postdate2 = "2023-03-20T21:00:15.000+09:00";
        String bloggerLink2 = "blog.naver.com/hdk208";

        String lastBuildDate = "2023-03-20";
        int total = 999;
        int start = 51;
        int display = 10;

        SearchBlogReq searchBlogReq = SearchBlogReq.builder()
                .page(page)
                .size(size)
                .sortType(sortType)
                .query(query)
                .build();

        List<NaverSearchBlogRes.Item> items = new ArrayList<>();
        items.add(NaverSearchBlogRes.Item.builder()
                .title(title1)
                .description(description1)
                .link(link1)
                .bloggerName(bloggerName1)
                .postdate(postdate1)
                .bloggerLink(bloggerLink1)
                .build());

        items.add(NaverSearchBlogRes.Item.builder()
                .title(title2)
                .description(description2)
                .link(link2)
                .bloggerName(bloggerName2)
                .postdate(postdate2)
                .bloggerLink(bloggerLink2)
                .build());

        NaverSearchBlogRes naverSearchBlogRes = NaverSearchBlogRes.builder()
                .lastBuildDate(lastBuildDate)
                .total(total)
                .start(start)
                .display(display)
                .items(items)
                .build();

        PageInfo pageInfo = PageInfo.builder()
                .sortType(sortType)
                .pageSize(PageUtil.pageSize(total, size))
                .pageNumber(page)
                .totalCount(total)
                .size(2)
                .isEnd(false)
                .build();

        List<BlogInfo> blogInfoList = new ArrayList<>();
        blogInfoList.add(BlogInfo.builder()
                .title(title1)
                .contents(description1)
                .url(link1)
                .blogName(bloggerName1)
                .dateTime(postdate1)
                .bloggerLink(bloggerLink1)
                .build());

        blogInfoList.add(BlogInfo.builder()
                .title(title2)
                .contents(description2)
                .url(link2)
                .blogName(bloggerName2)
                .dateTime(postdate2)
                .bloggerLink(bloggerLink2)
                .build());

        SearchBlogRes expect = SearchBlogRes.builder()
                .blogType(BlogType.NAVER)
                .pageInfo(pageInfo)
                .blogInfoList(blogInfoList)
                .build();

        //when
        SearchBlogRes result = naverSearchBlogRes.toSearchBlogRes(searchBlogReq);

        //then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }
}