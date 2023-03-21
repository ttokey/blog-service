package com.ttokey.blog.controller;

import com.epages.restdocs.apispec.ConstrainedFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.ttokey.blog.dto.BlogInfo;
import com.ttokey.blog.dto.PageInfo;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.dto.SearchBlogRes;
import com.ttokey.blog.enumeration.BlogType;
import com.ttokey.blog.enumeration.SortType;
import com.ttokey.blog.service.SearchService;
import com.ttokey.blog.util.PageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(SearchController.class)
public class SearchControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    public void searchBlogTest() throws Exception {
        //given
        String query = "카카오";
        SortType sortType = SortType.ACCURACY;
        int size = 3;
        int page = 1;
        int pageableCount = 436;
        boolean isEnd = false;

        SearchBlogReq searchBlogReq = SearchBlogReq.builder()
                .query(query)
                .sortType(sortType)
                .size(size)
                .page(page)
                .build();

        PageInfo pageInfo = PageInfo.builder()
                .sortType(sortType)
                .pageSize(PageUtil.pageSize(pageableCount, size))
                .pageNumber(page)
                .totalCount(pageableCount)
                .size(size)
                .isEnd(isEnd)
                .build();

        List<BlogInfo> blogInfoList = Arrays.asList(BlogInfoProperty.blogInfo1, BlogInfoProperty.blogInfo2, BlogInfoProperty.blogInfo3);
        SearchBlogRes searchBlogRes = SearchBlogRes.builder()
                .blogType(BlogType.KAKAO)
                .pageInfo(pageInfo)
                .blogInfoList(blogInfoList)
                .build();
        when(searchService.blogSearch(any(SearchBlogReq.class))).thenReturn(searchBlogRes);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", searchBlogReq.getQuery());
        params.add("sort", searchBlogReq.getSortType().getKakaoType());
        params.add("page", String.valueOf(searchBlogReq.getPage()));
        params.add("size", String.valueOf(searchBlogReq.getSize()));

        ConstrainedFields responseFields = new ConstrainedFields(SearchBlogRes.class);

        //when
        ResultActions result = this.mockMvc.perform(get("/v1/search/blog")
                .queryParams(params));


        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogType").value(searchBlogRes.getBlogType().getName()))
                .andExpect(jsonPath("$.pageInfo.sortType").value(searchBlogRes.getPageInfo().getSortType().getKakaoType()))
                .andExpect(jsonPath("$.pageInfo.pageSize").value(searchBlogRes.getPageInfo().getPageSize()))
                .andExpect(jsonPath("$.pageInfo.pageNumber").value(searchBlogRes.getPageInfo().getPageNumber()))
                .andExpect(jsonPath("$.pageInfo.totalCount").value(searchBlogRes.getPageInfo().getTotalCount()))
                .andExpect(jsonPath("$.pageInfo.size").value(searchBlogRes.getPageInfo().getSize()))
                .andExpect(jsonPath("$.pageInfo.isEnd").value(searchBlogRes.getPageInfo().getIsEnd()))
                .andExpect(jsonPath("$.blogInfoList", hasSize(blogInfoList.size())))
                .andExpect(jsonPath("$.blogInfoList[0].title").value(searchBlogRes.getBlogInfoList().get(0).getTitle()))
                .andExpect(jsonPath("$.blogInfoList[0].contents").value(searchBlogRes.getBlogInfoList().get(0).getContents()))
                .andExpect(jsonPath("$.blogInfoList[0].url").value(searchBlogRes.getBlogInfoList().get(0).getUrl()))
                .andExpect(jsonPath("$.blogInfoList[0].blogName").value(searchBlogRes.getBlogInfoList().get(0).getBlogName()))
                .andExpect(jsonPath("$.blogInfoList[0].dateTime").value(searchBlogRes.getBlogInfoList().get(0).getDateTime()))
                .andExpect(jsonPath("$.blogInfoList[0].thumbnail").value(searchBlogRes.getBlogInfoList().get(0).getThumbnail()))
                .andExpect(jsonPath("$.blogInfoList[0].bloggerLink").isEmpty())
                .andExpect(jsonPath("$.blogInfoList[1].title").value(searchBlogRes.getBlogInfoList().get(1).getTitle()))
                .andExpect(jsonPath("$.blogInfoList[1].contents").value(searchBlogRes.getBlogInfoList().get(1).getContents()))
                .andExpect(jsonPath("$.blogInfoList[1].url").value(searchBlogRes.getBlogInfoList().get(1).getUrl()))
                .andExpect(jsonPath("$.blogInfoList[1].blogName").value(searchBlogRes.getBlogInfoList().get(1).getBlogName()))
                .andExpect(jsonPath("$.blogInfoList[1].dateTime").value(searchBlogRes.getBlogInfoList().get(1).getDateTime()))
                .andExpect(jsonPath("$.blogInfoList[1].thumbnail").value(searchBlogRes.getBlogInfoList().get(1).getThumbnail()))
                .andExpect(jsonPath("$.blogInfoList[1].bloggerLink").isEmpty())
                .andExpect(jsonPath("$.blogInfoList[2].title").value(searchBlogRes.getBlogInfoList().get(2).getTitle()))
                .andExpect(jsonPath("$.blogInfoList[2].contents").value(searchBlogRes.getBlogInfoList().get(2).getContents()))
                .andExpect(jsonPath("$.blogInfoList[2].url").value(searchBlogRes.getBlogInfoList().get(2).getUrl()))
                .andExpect(jsonPath("$.blogInfoList[2].blogName").value(searchBlogRes.getBlogInfoList().get(2).getBlogName()))
                .andExpect(jsonPath("$.blogInfoList[2].dateTime").value(searchBlogRes.getBlogInfoList().get(2).getDateTime()))
                .andExpect(jsonPath("$.blogInfoList[2].thumbnail").value(searchBlogRes.getBlogInfoList().get(2).getThumbnail()))
                .andExpect(jsonPath("$.blogInfoList[2].bloggerLink").isEmpty())
                .andDo(document("kakao blog 검색 결과"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , resource(ResourceSnippetParameters.builder()
                                .summary("blog 검색")
                                .description("blog 검색")
                                .tag("Search")
                                .responseSchema(Schema.schema("SearchBlogRes"))
                                .queryParameters(
                                        parameterWithName("query").type(SimpleType.STRING).description("검색어"),
                                        parameterWithName("sort").type(SimpleType.STRING).description("정렬 방식").optional(),
                                        parameterWithName("page").type(SimpleType.STRING).description("페이지").optional(),
                                        parameterWithName("size").type(SimpleType.STRING).description("페이지 크기").optional()
                                )
                                .responseFields(
                                        responseFields.withPath("blogType").type(JsonFieldType.STRING).description("블로그 타입"),
                                        responseFields.withPath("pageInfo.sortType").type(JsonFieldType.STRING).description("정렬 방식"),
                                        responseFields.withPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        responseFields.withPath("pageInfo.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        responseFields.withPath("pageInfo.totalCount").type(JsonFieldType.NUMBER).description("전체 검색 결과 크기"),
                                        responseFields.withPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        responseFields.withPath("pageInfo.isEnd").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        responseFields.withPath("blogInfoList").type(JsonFieldType.ARRAY).description("검색 결과 리스트"),
                                        responseFields.withPath("blogInfoList[0].title").type(JsonFieldType.STRING).description("블로그 제목"),
                                        responseFields.withPath("blogInfoList[0].contents").type(JsonFieldType.STRING).description("블로그 내용"),
                                        responseFields.withPath("blogInfoList[0].url").type(JsonFieldType.STRING).description("블로그 주소"),
                                        responseFields.withPath("blogInfoList[0].blogName").type(JsonFieldType.STRING).description("블로그 명"),
                                        responseFields.withPath("blogInfoList[0].dateTime").type(JsonFieldType.STRING).description("글 생성 시간"),
                                        responseFields.withPath("blogInfoList[0].thumbnail").type(JsonFieldType.STRING).description("썸네일 주쇼").optional(),
                                        responseFields.withPath("blogInfoList[0].bloggerLink").type(JsonFieldType.STRING).description("블로그 대표 주소").optional()
                                )
                                .build())));
    }

    public final class BlogInfoProperty {
        public static String title1 = "본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션";
        public static String contents1 = "판매 수익금 기부, <b>카카오</b>톡 채널 추가 쿠폰 증정  https://naver.me/G5QhNN4b 본죽, 임영웅 광고 캠페인 공개 &amp; 할인 프로모션 본죽이 브랜드 모델 임영웅과 함께 한 광고를 공개하고 2주간 할인 행사를 진행한다고 20일 밝혔다. 내달 2일까지 배달앱 배달의민족에서 본죽 전 메뉴 구매 시 일일 선착순 4000명에게 최대...";
        public static String url1 = "https://blog.naver.com/ohmiy0727/223050498189";
        public static String blogName1 = "재미있는 이야기";
        public static String datetime1 = "2023-03-20T21:00:26.000+09:00";
        public static String thumbnail1 = "";
        public static String title2 = "[강남역] 에스프레소바 파라볼레 PARABOLE";
        public static String contents2 = "커다란 창이 나있어서 분위기가 좋다";
        public static String url2 = "http://starbongja.tistory.com/19";
        public static String blogName2 = "스타봉자";
        public static String datetime2 = "2023-03-20T21:00:15.000+09:00";
        public static String thumbnail2 = "https://search1.kakaocdn.net/argon/130x130_85_c/H26sb0LbQF1";
        public static String title3 = "재택 알바 사기, 통장을 이용하는 범죄";
        public static String contents3 = "가능이라고 적혀있으며 시급은 15,000원으로 나왔다 근무 상세내용에 대해서는 적혀있지 않았고 얼마 후 내 핸드폰 SMS으로 문자가 왔다 보통 사기꾼들은 <b>카카오</b>톡으로 보내달라고 하는 경우가 많은데 이 사기꾼은 일반문자로 대화를 했다 사기꾼은 사이트 url을 하나 알려주면서 100%는 이해되지 않지만 뭔가 그럴싸...";
        public static String url3 = "http://leader85.tistory.com/8";
        public static String blogName3 = "온라인 사기, 범죄";
        public static String datetime3 = "2023-03-20T23:45:21.000+09:00";
        public static String thumbnail3 = "https://search3.kakaocdn.net/argon/130x130_85_c/FrrVPjjz6iR";

        public static BlogInfo blogInfo1 = BlogInfo.builder()
                .title(title1)
                .contents(contents1)
                .url(url1)
                .blogName(blogName1)
                .thumbnail(thumbnail1)
                .dateTime(datetime1)
                .build();

        public static BlogInfo blogInfo2 = BlogInfo.builder()
                .title(title2)
                .contents(contents2)
                .url(url2)
                .blogName(blogName2)
                .thumbnail(thumbnail2)
                .dateTime(datetime2)
                .build();

        public static BlogInfo blogInfo3 = BlogInfo.builder()
                .title(title3)
                .contents(contents3)
                .url(url3)
                .blogName(blogName3)
                .thumbnail(thumbnail3)
                .dateTime(datetime3)
                .build();
    }
}
