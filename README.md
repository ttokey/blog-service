# blog-service

## jar파일 위치

https://github.com/ttokey/blog-service/blob/master/ttokey-blog-service-0.0.1-SNAPSHOT.jar

## API 설명

2가지 api를 제공하고 있다

1. blog 검색 서비스.
    1. 1차 검색 결과(카카오 블로그 검색 api)가 문제 있는 경우 2차 검색(네이버 블로그 검색 api)을 활용한다.
2. 검색어 순위서비스
    1. 상위 1 ~ 10위 까지의 검색 결과를 return 한다.

## API 명세

### 0. Swagger + RestAPI

서비스 실행 후 http://localhost:8080/swagger-ui.html 를 통해 API 명세 확인 및 테스트 가능

### 1. blog 검색 서비스

#### 기본 정보

```
GET /v1/search/blog HTTP/1.1
Host: localhost:8080
```

#### Request

##### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| query | String | 검색을 원하는 질의어| O |
| sort | String | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy | X |
| page | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1 | X |
| size | Integer | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10 | X |

#### Response

| Name | Type | Description                        |
| --- | --- |------------------------------------|
| blogType | String | 카카오 응답 : “kakao”  <br/>네이버 응답 : “naver” |
| pageInfo | PageInfo | 페이지 메타 정보                          |
| blogInfos | List<BlogInfo> | 블로그 검색 결과                          |

##### \<PageInfo\>

| Name | Type | Description |
| --- | --- | --- |
| sortType | String | 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순) |
| pageSize | Integer | 전체 페이지 크기 |
| pageNumber | Integer | 현재 페이지 번호 |
| totalCount | Integer | 노출 가능 문서 수 |
| size | Integer | 현재 페이지 검색 결과 갯수 |
| isEnd | Boolean | 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음 |

##### \<BlogInfo\>

| Name | Type | Description |
| --- | --- | --- |
| title | String | 블로그 글 제목 |
| contents | String | 블로그 글 요약 |
| url | String | 블로그 글 URL |
| blogName | String | 블로그의 이름 |
| thumbnail | String | 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음(카카오 검색 결과에만 존재) |
| dateTime | String | 블로그 글 작성시간 |
| bloggerLink | String | 블로그 홈 링크(네이버 검색 결과에만 존재) |

#### Sample

##### Request

```
curl -X 'GET' \
  'http://localhost:8080/v1/search/blog?query=카카오sort=accuracy&page=1&size=10' \
  -H 'accept: */*'
```

##### Response

```
{
  "blogType": "kakao",
  "pageInfo": {
    "sortType": "accuracy",
    "pageSize": 80,
    "pageNumber": 1,
    "totalCount": 800,
    "size": 10,
    "isEnd": false
  },
  "blogInfos": [
    {
      "title": "<b>카카오</b> 주가 전망",
      "contents": "아이위랩이라 하는 이름으로 설립되었으며, 2010년부터 <b>카카오</b> 브랜드 서비스를 시작해서 <b>카카오</b>아지트, <b>카카오</b>수다, <b>카카오</b>톡을 런칭했습니다. 자회사로 <b>카카오</b>게임즈, <b>카카오</b>뱅크, <b>카카오</b>페이, <b>카카오</b>엔터테인먼트 등을 보유하고 있어요. 그리고, <b>카카오</b>는 국내외 여러가지 분야에서 성장하고 있는 기업으로, 연락처...",
      "url": "http://boneung.worldentre.com/33",
      "blogName": "본능의",
      "dateTime": "2023-03-18T09:19:33.000+09:00",
      "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/9aMAH2QUDeo",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 주가 알아보기",
      "contents": "안녕하세요. 이번 포스팅에서는 <b>카카오</b> 주가 대해서 알아보려고 합니다. <b>카카오</b> 주가 삼성증권이 22일 <b>카카오</b>게임즈 (42,350원 ▲1,350 +3.29%)에 대한 투자의견을 &#39;중립&#39;에서 &#39;매수&#39;로 전환했다.목표주가를 기존 3만9000원에서 4만7000원으로 상향 조정했다. 현 주가가 모멘텀 소진으로 소폭 하락했지만 신작 출시로...",
      "url": "http://info.kralatom.com/30",
      "blogName": "다도리",
      "dateTime": "2023-03-22T13:30:23.000+09:00",
      "thumbnail": "https://search1.kakaocdn.net/argon/130x130_85_c/LNyt8Fbkvpj",
      "bloggerLink": null
    },
    {
      "title": "몰아치기, <b>카카오</b>게임즈 &#39;알찼다&#39;",
      "contents": "[한줄뉴스-3월 15일]아키에이지워, 신규 홍보 영상 공개 컴투스, 서머너즈워 크로니클 글로벌 순항 &#39;기념 이벤트&#39; 신작 이슈가 또 주춤했다. <b>카카오</b>게임즈는 멈추지 않았다. 야심작 &#39;아키에이지 워&#39;의 막바지 예열을 지속한 것. 3월 15일 수요일, <b>카카오</b>게임즈가 &#39;아키에이지 워&#39; 신규 홍보 영상을 공개했다. 대륙과...",
      "url": "http://gametalk.tistory.com/2469",
      "blogName": "게임앤드(gameand)",
      "dateTime": "2023-03-15T15:54:18.000+09:00",
      "thumbnail": "https://search2.kakaocdn.net/argon/130x130_85_c/Hl0mEvKOahs",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b>뱅크 전월세보증금 총정리",
      "contents": "그랬던 것처럼 보증금 낼 돈이 500만 원이 전부라면 어떻게 해야 할까요. 5분만 아니 3분만 보시면 두 번째 집으로 이사 가실 수 있게 도와드리겠습니다. <b>카카오</b>뱅크 전월세보증금 총정리 공인중개사 현직에서 일하다 보면 많은 분들이 <b>카카오</b>뱅크 전월세보증금에 대해 문의하십니다. 잠깐 살펴보면 너무 단순하고 간편...",
      "url": "http://hwshare07.com/48",
      "blogName": "하운드쉐어",
      "dateTime": "2023-03-19T12:43:37.000+09:00",
      "thumbnail": "",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 주가 주식 전망",
      "contents": "23.3.2(목) 지난 학기 대학교 교양은 아니지만 교양같은 과목에서 <b>카카오</b> 주가 오를 수 있을까? 라는 주제로 한동안 이야기했었던 것이 기억이 난다. 그만큼 한국에서는 IT에 관심이 있다면 한 두주씩은 가지고 있었을 <b>카카오</b> 주식. 현재 <b>카카오</b> 주식은 흐르고 있다. 물도 아닌데. (웃으라고 적어본 농담) <b>카카오</b> 주식...",
      "url": "http://zoosikk.tistory.com/563",
      "blogName": "거침없는 이야기",
      "dateTime": "2023-03-02T02:49:16.000+09:00",
      "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/1pt2NtSacH9",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 뱅크 적금 추천",
      "contents": "특판 상품도 점차 사라져 가고 있습니다. 요즘은 간편하게 가입하고 유지 조건도 까다롭지 않으면서 금리가 점점 더 어려워지고 있습니다. 반면, <b>카카오</b>뱅크는 대한민국 국민이라면 대부분 보유하고 있지 않을까 싶습니다. 오늘은 가장 인기 있는 <b>카카오</b> 뱅크 적금에 대해서 자세히 알아보겠습니다. 목차 1. <b>카카오</b> 뱅크...",
      "url": "http://d.yesomryu.com/10",
      "blogName": "KOREA DAILY NEWS",
      "dateTime": "2023-03-07T02:12:42.000+09:00",
      "thumbnail": "",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 브런치 작가 신청",
      "contents": "브런치는 <b>카카오</b>에서 운영하는 블로그예요! <b>카카오</b> 아이디만 있으면 가입할 수 있고 브런치 내 작가님들의 글을 무료로 볼 수 있어요 &#39;작가의 서랍&#39;이라는 공간에 글을 쓸 수 있지만 작가신청을 통해서 심사를 거친 뒤 작가가 되어야 남들에게 제가 쓴 글을 보여줄 수 있어요 작가신청 하게 된 이유 친구와의 대화 창인...",
      "url": "http://1sangjongm1n.tistory.com/9",
      "blogName": "일상속의종민",
      "dateTime": "2023-02-22T15:28:57.000+09:00",
      "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/Cj8lzcCMiyD",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b>닙스 다크초콜릿 효능 및 부작용",
      "contents": "최근 <b>카카오</b>닙스 이야기를 많이 들어보셨을텐테요. 그리고 달콤한게 먹고 싶은데 초콜릿은 조금 부담이 되어 <b>카카오</b> 함량이 높은 다크초콜릿을 찾는 분들도 점점 늘어나고 있는거 같아요. 그래서 이번엔 <b>카카오</b>닙스와 다크초콜릿 효능과 함량에 대하여 이야기를 해보도록 할게요 <b>카카오</b>닙스란? <b>카카오</b>빈을 건조, 발효...",
      "url": "http://confi.confidence300.co.kr/37",
      "blogName": "가르침",
      "dateTime": "2023-02-28T17:31:30.000+09:00",
      "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/K3pOBhkJmDK",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 로그인 API 사용하기(feat.리액트)",
      "contents": "<b>Kakao</b> Developers <b>카카오</b> API를 활용하여 다양한 어플리케이션을 개발해보세요. <b>카카오</b> 로그인, 메시지 보내기, 친구 API, 인공지능 API 등을 제공합니다. developers.kakao.com <b>카카오</b> 로그인 API를 사용해 간단한 로그인을 구현해보고 싶다는 생각이 들었다. 곧바로 <b>카카오</b> 개발자 사이트에 들어가 로그인 관련 API...",
      "url": "http://yankim.tistory.com/181",
      "blogName": "FE 개발자 성장일지 🎁",
      "dateTime": "2023-03-10T18:52:01.000+09:00",
      "thumbnail": "https://search1.kakaocdn.net/argon/130x130_85_c/HtP5qPUKfw8",
      "bloggerLink": null
    },
    {
      "title": "<b>카카오</b> 검색의 다다음 ChatGPT 베타서비스 출시",
      "contents": "● 비 디스커버 지난번에 글 썼던 <b>카카오</b>브레인의 인공지능 앱: 비 디스커버 <b>카카오</b>브레인의 인공지능 앱: 비 디스커버 신기한 것을 발견하여 주주방에 소개했던 걸 갈무리해서 글 올립니다. ● 먼저 보실까요? 이게 뭘까요? 이게 뭘까요? 카아아카콥? #<b>카카오</b>브레인 (#<b>카카오</b> 자회사)에서 만든 앱으로 인공지능으로...",
      "url": "http://lecos.tistory.com/591",
      "blogName": "넷꾼 투자이야기",
      "dateTime": "2023-03-19T03:50:49.000+09:00",
      "thumbnail": "",
      "bloggerLink": null
    }
  ]
}
```

---

### 2. 검색어 순위 서비스

#### 기본 정보

```
GET /v1/search/top-ten-word HTTP/1.1
Host: localhost:8080
```

#### Response

| Name | Type | Description |
| --- | --- | --- |
| topTenWordInfos | List<TopTenWordInfo> | 상위 10개 검색어 결과 |

##### \<TopTenWordInfo\>

| Name | Type | Description |
| --- | --- | --- |
| word | String | 검색어 |
| count | Long | 조회수 |

#### Sample

##### Request

```
curl -X 'GET' \
  'http://localhost:8080/v1/search/top-ten-word' \
  -H 'accept: */*'
```

##### Response

```
{
  "topTenWordInfos": [
    {
      "word": "주식",
      "count": 13
    },
    {
      "word": "라인",
      "count": 12
    },
    {
      "word": "카카오",
      "count": 11
    },
    {
      "word": "구글",
      "count": 10
    },
    {
      "word": "쿠팡",
      "count": 9
    },
    {
      "word": "배달의민족",
      "count": 7
    },
    {
      "word": "네이버",
      "count": 6
    },
    {
      "word": "페이스북",
      "count": 6
    }
  ]
}
```

---

## 사용한 라이브러리

### spring boot

- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation

### spring cloud

- spring-cloud-starter-openfeign
    - http 통신을 위함
- spring-cloud-starter-circuitbreaker-resilience4j
    - 요청 실패시 작업을 정의하기 위한 circuit breaker 사용

### retry

- spring-retry:2.0.0, spring-aspects:6.0.6
    - 메서드 내 exception 발생 시 재실행

### database

- h2database:h2
    - 인메모리 DataBase 제공

### util

- org.projectlombok:lombok

### test

- spring-boot-starter-test
    - junit 및 mockito를 통한 테스트 코드 작성 위함

### swagger + restdocs

- springdoc-openapi-starter-webmvc-ui:2.0.4
    - json rest doc 혁식을 swagger로 띄우기 위함
- restdocs:spring-restdocs-mockmvc
    - spring rest doc 작성
- restdocs-api-spec-mockmvc:0.17.1
    - rest doc을 json 으로 작성





