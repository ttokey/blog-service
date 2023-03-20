package com.ttokey.blog.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttokey.blog.dto.SearchBlogReq;
import com.ttokey.blog.enumeration.SortType;
import com.ttokey.blog.enumeration.TtokeyErrorType;
import com.ttokey.blog.exception.TtokeyException;
import com.ttokey.blog.feign.dto.KakaoSearchBlogReq;
import com.ttokey.blog.feign.dto.KakaoSearchBlogRes;
import com.ttokey.blog.feign.dto.NaverSearchBlogReq;
import com.ttokey.blog.feign.dto.NaverSearchBlogRes;
import com.ttokey.blog.feign.dto.SearchBlog;
import com.ttokey.blog.property.SearchAuthorizationProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class SearchBlogClientTest {
    @MockBean
    KaKaoSearchBlogClient kaKaoSearchBlogClient;
    @MockBean
    NaverSearchBlogClient naverSearchBlogClient;

    @Autowired
    SearchBlogClient searchBlogClient;

    KakaoSearchBlogRes kakaoSearchBlogRes;
    NaverSearchBlogRes naverSearchBlogRes;

    SearchBlogReq searchBlogReq;

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        kakaoSearchBlogRes = objectMapper.readValue(JsonProperty.KAKAO_JSON, KakaoSearchBlogRes.class);
        naverSearchBlogRes = objectMapper.readValue(JsonProperty.NAVER_JSON, NaverSearchBlogRes.class);

        searchBlogReq = SearchBlogReq.builder()
                .query("카카오")
                .sortType(SortType.ACCURACY)
                .size(10)
                .page(3)
                .build();
    }

    @Test
    @DisplayName("써킷브레이커 미동작 - 카카오 호출")
    public void no_use_circuitBreaker() {
        //given
        KakaoSearchBlogReq kakaoSearchBlogReq = KakaoSearchBlogReq.builder().searchBlogReq(searchBlogReq).build();
        when(kaKaoSearchBlogClient.blogSearch(kakaoSearchBlogReq, SearchAuthorizationProperty.KAKAO_AUTH))
                .thenReturn(kakaoSearchBlogRes);
        SearchBlog expect = kakaoSearchBlogRes;

        //when
        SearchBlog result = searchBlogClient.searchBlog(kakaoSearchBlogReq);

        //then
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("써킷브레이커 동작 - 네이버 호출")
    public void use_circuitBreaker() {
        //given
        KakaoSearchBlogReq kakaoSearchBlogReq = KakaoSearchBlogReq.builder().searchBlogReq(searchBlogReq).build();
        when(kaKaoSearchBlogClient.blogSearch(kakaoSearchBlogReq, SearchAuthorizationProperty.KAKAO_AUTH))
                .thenThrow(new TestException());
        when(naverSearchBlogClient.blogSearch(any(NaverSearchBlogReq.class), eq(SearchAuthorizationProperty.NAVER_CLIENT_ID), eq(SearchAuthorizationProperty.NAVER_CLIENT_SECRET)))
                .thenReturn(naverSearchBlogRes);
        SearchBlog expect = naverSearchBlogRes;

        //when
        SearchBlog result = searchBlogClient.searchBlog(kakaoSearchBlogReq);

        //then
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("써킷브레이커 동작 - 네이버 호출실패")
    public void use_circuitBreaker_naver_fail() {
        //given
        KakaoSearchBlogReq kakaoSearchBlogReq = KakaoSearchBlogReq.builder().searchBlogReq(searchBlogReq).build();
        when(kaKaoSearchBlogClient.blogSearch(kakaoSearchBlogReq, SearchAuthorizationProperty.KAKAO_AUTH))
                .thenThrow(new TestException());
        when(naverSearchBlogClient.blogSearch(any(NaverSearchBlogReq.class), eq(SearchAuthorizationProperty.NAVER_CLIENT_ID), eq(SearchAuthorizationProperty.NAVER_CLIENT_SECRET)))
                .thenThrow(new TtokeyException(TtokeyErrorType.BLOG_SEARCH_FAIL));

        //when

        //then
        Assertions.assertThatThrownBy(() -> searchBlogClient.searchBlog(kakaoSearchBlogReq))
                .hasMessage(TtokeyErrorType.BLOG_SEARCH_FAIL.getMessage())
                .isInstanceOf(TtokeyException.class);
    }

    public class TestException extends RuntimeException {
    }

    public static final class JsonProperty {
        public static String NAVER_JSON = "{\n" +
                "    \"lastBuildDate\": \"Mon, 20 Mar 2023 23:49:22 +0900\",\n" +
                "    \"total\": 14296308,\n" +
                "    \"start\": 1,\n" +
                "    \"display\": 10,\n" +
                "    \"items\": [\n" +
                "        {\n" +
                "            \"title\": \"<b>카카오</b>뱅크 비상금대출 조건 연장 이자 어떻게 낮출까?\",\n" +
                "            \"link\": \"https://blog.naver.com/topjoys/223045166670\",\n" +
                "            \"description\": \"특히 경제 금융 전문 블로그를 운영하는 제 입장에서는 금융 소비자 동태를 파악하고 있어서 <b>카카오</b>뱅... <b>카카오</b>뱅크 비상금대출 조건은? 기본적으로 1금융권에서 제공하는 다양한 소액여신 상품이 굉장히... \",\n" +
                "            \"bloggername\": \"경제 비서 인퐁\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/topjoys\",\n" +
                "            \"postdate\": \"20230315\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"<b>카카오</b>톡 복구 진행했던 방법\",\n" +
                "            \"link\": \"https://blog.naver.com/kiki1431/223019123185\",\n" +
                "            \"description\": \"백업해 둔 데이터가 있다면 가능했겠지만 그게 없는 지금 혼자서 해볼 수 있는 <b>카카오</b>톡 복구 방법은... 통해 <b>카카오</b>톡 복구 문제를 의뢰하는 방법이 있다고해서 알아보았는데요. 좋은 정보라 후기 남기려고... \",\n" +
                "            \"bloggername\": \"꾹이의 IT 이야기\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/kiki1431\",\n" +
                "            \"postdate\": \"20230217\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"주민등록등본 모바일 <b>카카오</b>톡으로 1분만에 발급 및 제출 방법\",\n" +
                "            \"link\": \"https://blog.naver.com/niolpa/223049018052\",\n" +
                "            \"description\": \"등본 발급이 모바일 <b>카카오</b>톡 앱에서 가능하다는 글을 전에 본 적이 있어서 키즈카페에서 바로 해봤습니다. 참고로 <b>카카오</b>톡 외에도 네이버 앱으로도 발급이 가능하다고 하는데요. 다음번에는 네이버 앱으로도... \",\n" +
                "            \"bloggername\": \"송도고래의 투자 이야기\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/niolpa\",\n" +
                "            \"postdate\": \"20230319\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"<b>카카오</b>페이 결제 현금영수증 신청 사용법 선물하기 유저 필수\",\n" +
                "            \"link\": \"https://blog.naver.com/lucky_box7/223049257528\",\n" +
                "            \"description\": \"등 <b>카카오</b>페이 사용은 일상 속에 완전히 자리 잡았습니다. 특히 기프티콘을 주고 받는 일이 많아지면서 선물하기 결제를 정말 자주 하게 되는데요. 오늘은 연말정산, 소득공제에 필수인 <b>카카오</b>페이 결제... \",\n" +
                "            \"bloggername\": \"Cwon&apos;s Life\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/lucky_box7\",\n" +
                "            \"postdate\": \"20230319\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"<b>카카오</b>페이 앱 사용법 정리(계좌 연결, 송금 및 취소)\",\n" +
                "            \"link\": \"https://blog.naver.com/supapa13/223040518712\",\n" +
                "            \"description\": \"본 포스팅은 점유율이 상당히 높아진 앱, <b>카카오</b>페이의 사용법입니다. 계좌 연결과 송금, 송금 취소에 대한 내용으로 구성됐습니다. 계좌 연결 <b>카카오</b>페이 계좌 연결은 이렇게 한다. 우선 카톡 앱을 오픈한다.... \",\n" +
                "            \"bloggername\": \"세수하면이병헌 IT, 육아\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/supapa13\",\n" +
                "            \"postdate\": \"20230312\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"왠수같은 내 주식 첫번째 이야기. <b>카카오</b>와 2년째\",\n" +
                "            \"link\": \"https://blog.naver.com/moonlit_jk/223049260271\",\n" +
                "            \"description\": \"#<b>카카오</b> 와 함께한지 2년이 다 되어간다. 존버중 처음 <b>카카오</b>를 매수한게 5월 28일 처음에 비중은 많이 안들어가 있었는데 이후에 #추매 를 하고 #수익 도 났었다. 장기로 생각하고 들어갔던 종목이라 안팔고... \",\n" +
                "            \"bloggername\": \"樂\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/moonlit_jk\",\n" +
                "            \"postdate\": \"20230319\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"<b>카카오</b>뱅크 비상금대출 조건, 금리 및 한도 설명!\",\n" +
                "            \"link\": \"https://blog.naver.com/haerieva/223048856213\",\n" +
                "            \"description\": \"그런 고충을 <b>카카오</b>뱅크가 잘 알고 있는지, <b>카카오</b>뱅크 비상금대출이라는 서비스를 제공하고 있죠. 금일은 <b>카카오</b>뱅크 비상금대출 조건, 금리 및 한도에 대해 설명드리고자 합니다. 참고로 저는 <b>카카오</b>뱅크... \",\n" +
                "            \"bloggername\": \"30대 부린이 & 주린이 성장일기\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/haerieva\",\n" +
                "            \"postdate\": \"20230319\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"[에스엠] 이수만의 가처분 신청 인용. <b>카카오</b> 포기할거임?\",\n" +
                "            \"link\": \"https://blog.naver.com/upower/223034271867\",\n" +
                "            \"description\": \"배제하고 <b>카카오</b>에 신주 및 전환사채를 발행해 약 2172억 규모의 자금을 반드시 긴급하게 조달해야 할... 하이브가 <b>카카오</b>에 대항하여 어떻게 자금조달을 할 것인가에 대해 고민하고 있었는데, 이번 인용 판결로... \",\n" +
                "            \"bloggername\": \"주식 낙서장\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/upower\",\n" +
                "            \"postdate\": \"20230304\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"에스엠, <b>카카오</b>의 공개 매수(15만원) / 현재 주가에 따른 매매... \",\n" +
                "            \"link\": \"https://blog.naver.com/0bin823/223043555844\",\n" +
                "            \"description\": \"https://www.mk.co.kr/news/it/10677710 &quot;SM엔터테인먼트를 둘러싼 <b>카카오</b>와 하이브 간 인수 경쟁이 극적으로 타결되면서 해당 기업 간 협력 구도에 귀추가 주목되고 있다. <b>카카오</b>와 하이브는 당분간 '따로 또 같이... \",\n" +
                "            \"bloggername\": \"와이빈의 블로그\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/0bin823\",\n" +
                "            \"postdate\": \"20230313\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"title\": \"애드센스 <b>카카오</b>뱅크 입금 MTCN번호 없어도 됩니다\",\n" +
                "            \"link\": \"https://blog.naver.com/ilchilife/223045196271\",\n" +
                "            \"description\": \"포스팅에서는 <b>카카오</b>뱅크로 입금받을 때 난감했던 MTCN번호 문제를 말씀드려볼게요 MTCN 번호 필요? 결론부터 말씀드리면 무시하고 기다리면 알아서 직원이 확인 후 입금됩니다 잘 모르는 처음에는 <b>카카오</b>뱅크... \",\n" +
                "            \"bloggername\": \"일치의 소소한 일상 리빙 블로그\",\n" +
                "            \"bloggerlink\": \"blog.naver.com/ilchilife\",\n" +
                "            \"postdate\": \"20230316\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        public static String KAKAO_JSON = "{\n" +
                "    \"documents\": [\n" +
                "        {\n" +
                "            \"blogname\": \"시안\",\n" +
                "            \"contents\": \"출처 기재 필수(시안) * 커미션 외 개인제작 굿즈에 대하여 * &gt; 별도로 글 작성(색입힌 글씨 클릭 시 이동) #. Etc 2차 : 뜰팁(공룡 위주), 팀샐러드, 안즈(<b>카카오</b>페이지), 좀비고등학교, 프로젝트 세카이(니고&gt;&gt;타유닛 여캐 위주), 엘소드, 블루아카이브(세리나, 코하루+티파티 위주) 창작종족 : 향초사슴 / 레피르...\",\n" +
                "            \"datetime\": \"2023-03-20T23:48:00.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search4.kakaocdn.net/argon/130x130_85_c/9vUEk9MkM2P\",\n" +
                "            \"title\": \"\uD835\uDC46\uD835\uDC3B\uD835\uDC3C\uD835\uDC34\uD835\uDC41\",\n" +
                "            \"url\": \"http://shina0129.tistory.com/5\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"레아미디어\",\n" +
                "            \"contents\": \"자신이 어떤 기버 행동을 했는지 기억해보아라. 책을 덮고 10분간 산책을 나가서 생각에 잠기는 것도 좋다. 최근에 본인 인생에 가장 큰 영향을 준 사람에게 <b>카카오</b>톡 선물하기를 통해 선물을 보내거나 돈을 송금하라. 혹은 상대가 어려워 보이는 점이 있다면 나름대로 해결책을 적어서 보내주어라. 승률이 있으면 손실...\",\n" +
                "            \"datetime\": \"2023-03-20T23:47:08.000+09:00\",\n" +
                "            \"thumbnail\": \"\",\n" +
                "            \"title\": \"0320 역행자 마음에 들었던 구절\",\n" +
                "            \"url\": \"http://leamedia.tistory.com/5\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"온라인 사기, 범죄\",\n" +
                "            \"contents\": \"가능이라고 적혀있으며 시급은 15,000원으로 나왔다 근무 상세내용에 대해서는 적혀있지 않았고 얼마 후 내 핸드폰 SMS으로 문자가 왔다 보통 사기꾼들은 <b>카카오</b>톡으로 보내달라고 하는 경우가 많은데 이 사기꾼은 일반문자로 대화를 했다 사기꾼은 사이트 url을 하나 알려주면서 100%는 이해되지 않지만 뭔가 그럴싸...\",\n" +
                "            \"datetime\": \"2023-03-20T23:45:21.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/130x130_85_c/FrrVPjjz6iR\",\n" +
                "            \"title\": \"재택 알바 사기, 통장을 이용하는 범죄\",\n" +
                "            \"url\": \"http://leader85.tistory.com/8\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"일본상품 뭐든지 문의해주세요 현아네 만물상입니다\",\n" +
                "            \"contents\": \"100％ &amp; 400％ \u200B 문의 바랍니다. 가격 문의와 정확한 구매절차는 반드시 저희 대표전화(070-7792-7723)으로 문의해주십시오. 또는 핸드폰 번호를 남겨주시면 <b>카카오</b>톡으로 자세한 답변드립니다. 블로그 댓글로는 더 이상 가격 정보를 오픈하기가 곤란해져서 부득이하게 방침을 변경하였습니다. \u200B ***님이 미개봉 신품을...\",\n" +
                "            \"datetime\": \"2023-03-20T23:45:00.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/9Ycp9fagh8u\",\n" +
                "            \"title\": \"메디콤토이 마이 퍼스트 마블(대리석) 100% &amp; 400% 일본 직배송료 포함 MY FIRST BE@RBRICK B@BY MARBLE...\",\n" +
                "            \"url\": \"https://blog.naver.com/hyunanemms/223050705174\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"정보핵심\",\n" +
                "            \"contents\": \"들어갑니다. 2. 로그인을 합니다. 일반 사람들은 개인회원으로 로그인을 하시면 되시는데, 회원가입을 먼저 진행해 주시고 로그인을 하시는 것이 좋습니다. <b>카카오</b>톡 로그인도 가능하니 회원가입이 번거로우신 분들은 <b>카카오</b>톡 회원가입을 진행하시면 되겠습니다. 3. 로그인을 하시고 발급신청에 들어가셔서 순서대로...\",\n" +
                "            \"datetime\": \"2023-03-20T23:41:38.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/130x130_85_c/DamVl61RlE1\",\n" +
                "            \"title\": \"내일배움카드 신청자격 총정리 (2023년)\",\n" +
                "            \"url\": \"http://best.no1goodinfo.com/22\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"Develop Lab\",\n" +
                "            \"contents\": \"매칭 받으세요. programmers.co.kr 문제 해석 문제 자체가 불 친절해서 해석을 제대로 하지 않는다면 제대로 된 정답을 뽑아내기 힘들수 있다고 생각했다. <b>카카오</b>에서 나온 문제랑 비슷한 문제가 있는데 상하좌우 한 칸씩 이동하는 것이 아니고 이동 할 수 있는 최대 거리로 이동하는 경우를 구하는 문제였다. 마찬가지...\",\n" +
                "            \"datetime\": \"2023-03-20T23:41:04.000+09:00\",\n" +
                "            \"thumbnail\": \"\",\n" +
                "            \"title\": \"프로그래머스 [Level 2] 리코쳇 로봇 (JAVA)\",\n" +
                "            \"url\": \"http://eno1993.tistory.com/223\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"리뷰노마드의 별 다섯개 짜리 인생컨텐츠\",\n" +
                "            \"contents\": \"저만 봣엇어서인지 힙스터 됏다고 ㅋㅋㅋㅋ 앜 원래... 민트초록이였는데 말이징!!! 그치만 이젠 물빠지고 오렌지 옐로우 되엇습니다 (tmi) \u200B \u200B 마루이 소다/<b>카카오</b> 핫 곰돌이/ 밀크티 0.7 \u200B 제일 유명하대요! \u200B 매장 이용시에는 1인 1메뉴 시키기! \u200B \u200B \u200B 그리고 디저트류가 진짜 귀여웟어요!!! 꺄아.... 밥 안먹고 빵...\",\n" +
                "            \"datetime\": \"2023-03-20T23:41:00.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/4iDZhhG7Thm\",\n" +
                "            \"title\": \"서면놀거리 데이트로 가볼만한곳 케이크 카페 야무야무\",\n" +
                "            \"url\": \"https://blog.naver.com/esther051/223050701060\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"영의 일지\",\n" +
                "            \"contents\": \"않다. 3주차가 되니 강의가 반복 되는 느낌인데, 아직 3주차는 강의를 시작만 한 상태니까 일단 지켜보기로 했다. 3주차 미션 아이데이션 스터디 주제: <b>카카오</b>톡과 같은 앱 서비스를 런칭하기 위해 고려해야 할 사항 5가지 이상 정리하 3주차 블로그 미션 어플 하나를 상세하게 기획을 뜯어보기: 어플 선택 이유, 기획...\",\n" +
                "            \"datetime\": \"2023-03-20T23:40:16.000+09:00\",\n" +
                "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/130x130_85_c/3eJ9KHwT6L1\",\n" +
                "            \"title\": \"제로베이스 PM 스쿨 : 0317 학습일기 (3주차-1DAY)\",\n" +
                "            \"url\": \"http://veryveryounglog.tistory.com/14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"money-digger\",\n" +
                "            \"contents\": \"경제 뉴스는 다음과 같습니다. 코로나19 확산에도 한국 경제 성장률 3.9% 전망…IMF 한국은행, 기준금리 0.25%포인트 인상 결정…2.00%로 삼성전자, 2023년 첫 분기 실적 발표…영업이익 12조원 예상 LG전자, 자율주행차 사업 본격화…SK 협력체계 구축 네이버, <b>카카오</b>와의 경쟁 격화에도 국내 최대 온라인 플랫폼 유지\",\n" +
                "            \"datetime\": \"2023-03-20T23:40:01.000+09:00\",\n" +
                "            \"thumbnail\": \"\",\n" +
                "            \"title\": \"2023년 3월 20일 한국\",\n" +
                "            \"url\": \"http://money-digger.tistory.com/8\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"blogname\": \"하루하루\",\n" +
                "            \"contents\": \"스케쥴 03월 21일 화요일 1. 미국) 2월 기존주택판매(현지시간) 2. 일본 증시 휴장 3. 애플페이, 국내 서비스 개시 예정 4. 국제물산업박람회 개최 예정 5. <b>카카오</b>게임즈, 아키에이지 워 출시 예정 6. 트와이스, 서울 공연 티켓 예매 시작 예정 7. 현대로템, 호주서 전동차 수주 &#39;대박&#39; 보도에 대한 조회 공시 답변...\",\n" +
                "            \"datetime\": \"2023-03-20T23:39:53.000+09:00\",\n" +
                "            \"thumbnail\": \"\",\n" +
                "            \"title\": \"2023년 3월 20일 월요일 일지\",\n" +
                "            \"url\": \"http://haruharu0024.tistory.com/61\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"meta\": {\n" +
                "        \"is_end\": false,\n" +
                "        \"pageable_count\": 800,\n" +
                "        \"total_count\": 17281639\n" +
                "    }\n" +
                "}";
    }


}