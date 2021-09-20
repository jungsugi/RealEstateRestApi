<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>README.md</title>
  <link rel="stylesheet" href="https://stackedit.io/style.css" />
</head>

<body class="stackedit">
  <div class="stackedit__html"><h1 id="카카오페이-사전과제---부동산신용-투자-서비스-rest-api">카카오페이 사전과제 - 부동산/신용 투자 서비스 REST API</h1>
<h2 id="목차">목차</h2>
<ul>
<li><a href="#%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD">개발 환경</a></li>
<li><a href="#%EB%B9%8C%EB%93%9C-%EB%B0%8F-%EC%8B%A4%ED%96%89%ED%95%98%EA%B8%B0">빌드 및 실행하기</a></li>
<li><a href="#%EA%B8%B0%EB%8A%A5-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD">기능 요구사항</a></li>
<li><a href="#%ED%95%B4%EA%B2%B0%EB%B0%A9%EB%B2%95">기능 요구사항 별 해결방법</a></li>
<li><a href="#%EB%8F%84%ED%81%90%EB%A8%BC%ED%8A%B8">API Document</a></li>
</ul>
<hr>
<h2 id="개발-환경">개발 환경</h2>
<ul>
<li>기본 환경
<ul>
<li>IDE: STS</li>
<li>OS: Window 10</li>
<li>GIT</li>
</ul>
</li>
<li>Server
<ul>
<li>Java8</li>
<li>Spring Boot 2.2.2</li>
<li>Spring Data JPA</li>
<li>MYSQL</li>
<li>Gradle</li>
<li>Junit5</li>
</ul>
</li>
</ul>
<h2 id="빌드-및-실행하기">빌드 및 실행하기</h2>
<h3 id="터미널-환경">터미널 환경</h3>
<ul>
<li>Git, Java 는 설치되어 있다고 가정한다.</li>
</ul>
<pre><code>$ git clone https://github.com/kakaopaycoding-server/202107-sooyoung199-naver.com.git
$ cd 202107-sooyoung199-naver.com
$ ./gradlew clean build
$ java -jar build\libs\202107-sooyoung199-naver.com-0.0.1-SNAPSHOT.jar
</code></pre>
<ul>
<li>접속 Base URI: <code>http://localhost:8080</code></li>
</ul>
<h2 id="기능-요구사항">기능 요구사항</h2>
<h3 id="공통사항">공통사항</h3>
<p><strong>전체투자상품조회, 투자하기, 나의투자상품조회API 구현</strong></p>
<ul>
<li>요청한 사용자 식별값은 숫자형태이며 "X-USER-ID"라는HTTPHeader로전달.</li>
<li>작성한 어플리케이션이 다수의 서버에서 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계</li>
<li>각 기능 및 제약사항에 대한 단위 테스트를 반드시 작성</li>
</ul>
<h3 id="필수사항">필수사항</h3>
<p><strong>1. 전체 투자 상품 조회 API</strong></p>
<ul>
<li>상품모집기간(started_at,finished_at)내의상품만응답</li>
<li>전체투자상품응답은다음내용을포함</li>
<li>(상품ID,상품제목,총모집금액,현재모집금액,투자자수,투자모집상태(모집중,모집 완료),상품모집기간)</li>
</ul>
<p><strong>2. 투자하기 API</strong></p>
<ul>
<li>사용자 식별값, 상품 ID, 투자금액을 입력값으로 받는다.</li>
<li>총 투자모집 금액(total_investing_amount) 를 넘어서면 SOLD-OUT 상태를 응답.</li>
</ul>
<p><strong>3. 나의 투자상품 조회 API</strong></p>
<ul>
<li>내가 투자한 상품을 모두 반환한다.</li>
<li>나의 투자 상품 응답은 다음 내용을 포함한다.</li>
<li>상품ID, 상품제목, 총 모집금액, 나의 투자금액, 투자일시</li>
</ul>
<h2 id="해결방법">해결방법</h2>
<h3 id="공통-요구-사항">1. 공통 요구 사항</h3>
<ul>
<li>컨트롤러에서 사용자 식별값인 X-USER-ID 헤더값을 전달 받고, 중복되는 코드를 줄이기 위해 HandlerMethodArgumentResolver.class 를 상속받은 UserArgumentResolver.class를 Spring IOC에 추가해 매 요청마다 X_USER_ID 값이 DataBase에 존재하는지 확인.<br>
사용자가 식별된다면 UserDto를 통해 사용자 정보를 반환.</li>
</ul>
<pre><code>@Configuration
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
	
	private static final String X_USER_ID = "X-USER-ID";
	 
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
									NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
									
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Long userId = convertUserId(request.getHeader(X_USER_ID));
	...
</code></pre>
<ul>
<li>Custom Exception 을 구현해, Runtime 동안 발생할 수 있는 예외를 정의하고 Custom Exception 호출</li>
<li>(에러코드 및 메세지는 API Cocument를 통해 확인)</li>
</ul>
<pre><code>CustomErrorCode.class
CustomException.class
</code></pre>
<ul>
<li>작성한 어플리케이션이 다수의 서버에서 다수의 인스턴스로 동작하더라도 각각의 인스턴스 캐시를 통해 DB 부하를 줄이기 위해 Cloud My SQL 서버를 DataBase Server로 사용</li>
</ul>
<pre><code># mysql db setting
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://database-instance-1.crzvpbsuuz6a.ap-northeast-2.rds.amazonaws.com/kakaopay?zeroDateTimeBehavior=convertToNull&amp;useUnicode=yes&amp;characterEncoding=UTF-8&amp;connectTimeout=2000&amp;autoReconnect=true&amp;serverTimezone=Asia/Seoul&amp;useSSL=false
</code></pre>
<ul>
<li>
<p><strong>각 기능 및 제약사항에 대한 테스트</strong></p>
<ul>
<li>테스트 진행 시 , @BeforeEach를 통해 각각의 단위 테스트에 사용할 데이터를 직접 Insert하고 , 테스트가 끝나면 @Transactional 과 @Rollback 기능을 통해 사용했던 데이터를 Rollback 하는 방식으로 단위별 테스트 진행</li>
</ul>
</li>
<li>
<p><strong>각 기능 및 제약사항에 대한를 큰 단위 테스트 2가지 통합 테스트로 나눠 테스트 진행</strong></p>
<ul>
<li>
<p>DataBase Repository Tests : @DataJpaTest 기능을 통해 Table 별 CRUD 기능 테스트</p>
</li>
<li>
<p>Service Tests : 요구사항을 세부 기능으로 쪼개어 각 세부사항 기능별 테스트 구현</p>
</li>
<li>
<p>실제 Request 요청을 통한 통합테스트 : Spring Boot와 JPA 를 활용해 주 요구사항 기능 3가지 (전체투자상품조회, 투자하기, 나의투자상품조회) 통합 테스트</p>
</li>
</ul>
</li>
</ul>
<h3 id="기능-요구사항--전체투자상품조회-투자하기-나의투자상품조회">2. 기능 요구사항  (전체투자상품조회, 투자하기, 나의투자상품조회)</h3>
<p><strong>Spring Data JPA 를 통해 기능 구현</strong></p>
<dl>
<dt><strong>- 2-1. 전체투자상품조회</strong></dt>
<dd>JPQL을 통한 요구사항 쿼리를 실행하고  응답내용을 요구사항 DTO 클래스에 담아서 리턴<br>
(Request URL 및 response fields는 Document를 참고)</dd>
</dl>
<blockquote>
<p>구현로직 : 상품(product) 테이블의 투자시작일시(started_at) , 투자종료일(finished_at) 이 현재 기준에 있는 data를 모두 조회</p>
</blockquote>
<dl>
<dt><strong>- 2-2. 투자하기</strong></dt>
<dd>상품 (product), 투자정보(invest_info), user(사용자) 테이블의 Entity 관계를 통해 투자하기 기능 구현</dd>
</dl>
<blockquote>
<p>구현로직 :</p>
<ol>
<li>사용자가 있는지 Validation Check</li>
<li>상품 Validation Check<br>
: 투자시작일(started_at), 투자종료일(finished_at) 기간에 있는지<br>
: 해당 상품이 총 모집 금액을 도달해 투자가 마감되었는지 Check</li>
<li></li>
</ol>
</blockquote>
<p><strong>- 2-3. 나의투자상품조회</strong></p>
<h2 id="도큐먼트">도큐먼트</h2>
<blockquote>
<p><strong>/202107-sooyoung199-naver.com/ApiDocument.html 파일 참고</strong></p>
</blockquote>
</div>
</body>

</html>
