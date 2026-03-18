# 칵테일 레시피 추천 서비스 구현 계획

## 1. 프로젝트 구조

```
cocktail-be/
├── src/
│   ├── main/
│   │   ├── java/com/cock/cocktail/
│   │   │   ├── CocktailBeApplication.java
│   │   │   ├── web/                            # 웹 계층
│   │   │   │   ├── CocktailController.java
│   │   │   │   ├── HealthController.java
│   │   │   │   └── dto/                        # 웹 요청/응답 DTO (record)
│   │   │   │       ├── CocktailRecommendationRequest.java
│   │   │   │       ├── CocktailRecommendationResponse.java
│   │   │   │       ├── CocktailDto.java
│   │   │   │       └── ErrorResponse.java
│   │   │   ├── service/
│   │   │   │   └── KeywordAnalyzer.java        # 서비스 인터페이스
│   │   │   ├── infrastructure/
│   │   │   │   └── config_based_analyzer/
│   │   │   │       ├── ConfigBasedKeywordAnalyzer.java
│   │   │   │       └── DescriptorDefinition.java
│   │   │   ├── domain/                         # 도메인 모델
│   │   │   │   ├── Cocktail.java
│   │   │   │   ├── Ingredient.java
│   │   │   │   ├── DescriptorCode.java
│   │   │   │   ├── SensoryAxis.java
│   │   │   │   └── SensoryDescriptors.java
│   │   │   ├── repository/
│   │   │   │   └── CocktailRepository.java
│   │   │   ├── exception/                      # 예외 처리
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── InvalidRequestException.java
│   │   │   └── config/                         # 설정
│   │   │       └── WebConfig.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── keywords.yml
│   │       └── data.sql
│   └── test/
│       └── java/com/cock/cocktail/
│           ├── web/                            # 웹 계층 테스트
│           ├── application/                    # 서비스 인터페이스 테스트
│           ├── infrastructure/config_based_analyzer/
│           ├── domain/
│           └── CocktailBeApplicationTests.java
├── build.gradle
└── README.md
```

**구조 설명**
- `web/`: 웹 계층 (Controller, DTO)
- `web/dto/`: 웹 요청/응답 DTO - Java record로 구현
- `service/`: 서비스 인터페이스
- `infrastructure/config_based_analyzer/`: 설정 기반 키워드 분석기 구현
- `domain/`: 도메인 모델 (Cocktail, DescriptorCode, SensoryDescriptors)
- `exception/`: 예외 처리
- `config/`: 설정

## 2. 구현 단계 (TDD 방식)

### Phase 1: 프로젝트 기본 설정
**목표**: 프로젝트 구조 및 의존성 설정

**작업 내용**
- [ ] Gradle 의존성 추가
  - Spring Web
  - Lombok
  - Validation
  - Jackson (JSON 처리)
- [ ] application.properties 설정
- [ ] 기본 패키지 구조 생성

**테스트 작성**
- [ ] 애플리케이션 컨텍스트 로드 테스트
  - CocktailBeApplicationTests 작성
  - Spring Boot 애플리케이션 정상 실행 확인

**완료 조건**
- ✅ 모든 의존성이 정상적으로 추가됨
- ✅ 애플리케이션이 정상적으로 실행됨
- ✅ 테스트 통과

**예상 산출물**
- 업데이트된 build.gradle
- application.properties
- 기본 패키지 구조
- CocktailBeApplicationTests.java

---

### Phase 2: DTO 및 도메인 모델 구현
**목표**: API 요청/응답 및 도메인 객체 정의

**테스트 작성 (TDD)**
- [x] DTO 직렬화/역직렬화 테스트
  - JSON → DTO 변환 테스트
  - DTO → JSON 변환 테스트
- [x] Validation 테스트
  - query 필드 null 검증
  - query 필드 빈 문자열 검증
  - 정상 값 검증

**구현**
- [x] 웹 계층 DTO 구현 (Java record 사용)
  - CocktailRecommendationRequest (web/dto)
  - CocktailRecommendationResponse (web/dto)
  - CocktailDto (web/dto)
  - ErrorResponse (web/dto)
- [x] 도메인 모델 구현
  - Ingredient (domain) - record
  - Cocktail (domain) - 일반 클래스 (Lombok)
- [x] Validation 어노테이션 추가
  - @NotBlank 등

**완료 조건**
- ✅ 모든 DTO가 record로 구현됨
- ✅ 직렬화/역직렬화 테스트 통과
- ✅ Validation 테스트 통과

**예상 산출물**
- 5개 웹 계층 DTO (record)
- 2개 도메인 모델 (Ingredient: record, Cocktail: 일반 클래스)
- DTO 테스트 클래스

**구현 원칙**
- 웹 계층 DTO는 불변성을 위해 record 사용
- 도메인 모델은 비즈니스 로직 필요 시 일반 클래스 사용

---

### Phase 3: 칵테일 데이터 준비
**목표**: 추천에 사용할 칵테일 데이터 준비

**테스트 작성 (TDD)**
- [ ] CocktailRepository 테스트
  - SQL 초기 데이터 로드 테스트
  - 전체 칵테일 조회 테스트
  - ID로 칵테일 조회 테스트
  - descriptor code 기반 검색 테스트

**구현**
- [ ] 칵테일 데이터 구조 설계
  - ID, 이름, 재료, 레시피, sensory descriptor
- [ ] 초기 칵테일 데이터 작성 (SQL)
  - 10~20개 정도의 칵테일
  - descriptor code(`sweet`, `fruity` 등) 저장
- [ ] JPA + SQL 초기화 연동
  - `data.sql`로 초기 데이터 로드
  - `CocktailRepository`에서 sensory descriptor 검색 지원

**완료 조건**
- ✅ data.sql 파일 생성 (10~20개)
- ✅ CocktailRepository 구현 완료
- ✅ 모든 Repository 테스트 통과

**예상 산출물**
- data.sql 파일
- CocktailRepository.java
- CocktailRepositoryTest.java
- 10~20개 칵테일 데이터

**칵테일 데이터 예시**
```sql
INSERT INTO cocktail_sensory_descriptors (cocktail_id, axis, "value") VALUES (1, 'TASTE', 'sweet');
INSERT INTO cocktail_sensory_descriptors (cocktail_id, axis, "value") VALUES (1, 'AROMA', 'fruity');
INSERT INTO cocktail_sensory_descriptors (cocktail_id, axis, "value") VALUES (1, 'SENSATION', 'carbonated');
```

---

### Phase 3.5: Sensory Descriptor 모델링 (MVP 단순화)
**목표**: 감각 속성 체계를 도메인 모델로 정립

**배경**
- 맛 인지는 단일 축이 아닌 여러 감각 차원의 결합
- TASTE(기본미), AROMA(향), MOUTHFEEL(질감), SENSATION(자극감), IMPRESSION(인상)
- 각 축은 측정 방식과 확장 방식이 다름
- sweet(강도), lime(계층), smoky(강도), tingling(존재), refreshing(복합 인상)

**감각 축 분류 원칙**
```
TASTE (기본미)
- sweet, sour, bitter, salty
- 값 타입: SCALAR (강도 1-5)

AROMA (향)
- fruity > citrus > lime
- fruity > berry > strawberry
- smoky, herbal
- 값 타입: BINARY (존재) 또는 TAGGED (primary/secondary/hint)
- 계층 구조 지원 필요

MOUTHFEEL (질감)
- smooth, creamy, light, dry
- 값 타입: BINARY 또는 SCALAR

SENSATION (자극감)
- carbonated, tingling, warming, cooling
- 값 타입: BINARY 또는 SCALAR
- 주의: "톡 쏘는"은 여러 후보로 매핑 가능 (carbonated/tingling/spicy)

IMPRESSION (인상/분위기)
- refreshing, elegant, easy_to_drink
- 감각 데이터로 직접 환원 불가
- 복합 인상: refreshing = citrus + cooling + low-sweet + carbonation
```

**MVP 단순화 범위**
- ✅ DescriptorCode 값 객체 (axis + code)
- ✅ 설정 파일 기반 descriptor 관리 (keywords.yml)
- ✅ Cocktail.sensoryDescriptors(List<DescriptorCode>)로 연결
- ⏸ 강도(intensity) 값: 나중 구현
- ⏸ 역할(PRIMARY/SECONDARY/HINT): 나중 구현
- ⏸ 사용자 쿼리 다중 해석: 나중 구현

**테스트 작성 (TDD)**
- [ ] Descriptor 모델 테스트
  - axis별 descriptor 조회 테스트
  - 계층 구조 (parent-child) 테스트
  - 동의어 매칭 테스트
- [ ] CocktailDescriptor 연결 테스트
  - 칵테일별 descriptor 조회 테스트
  - descriptor로 칵테일 검색 테스트

**구현**
- [ ] DescriptorCode 도메인 모델
  - axis, value(code)
  - axis: TASTE, AROMA, MOUTHFEEL, SENSATION, IMPRESSION
- [ ] 키워드 동의어 모델 (설정 파일)
  - code + synonym 매핑
- [ ] keywords.yml 재구성
  - axis별로 분리
  - 계층 구조 표현
- [ ] ConfigBasedKeywordAnalyzer 수정
  - axis별 descriptor 추출
  - 계층 구조 고려한 매칭

**완료 조건**
- ✅ Descriptor 모델 구현 완료
- ✅ keywords.yml이 axis별로 재구성됨
- ✅ 계층 구조가 반영됨 (예: fruity > citrus > lime)
- ✅ ConfigBasedKeywordAnalyzer가 axis 분리 지원
- ✅ 모든 테스트 통과

**예상 산출물**
- DescriptorCode.java (도메인 모델)
- SensoryAxis.java (Enum: TASTE, AROMA, ...)
- keywords.yml (재구성)
- ConfigBasedKeywordAnalyzer.java (수정)
- DescriptorTest.java

**데이터 구조 예시**
```yaml
# keywords.yml
descriptors:
  taste:
    - code: "sweet"
      label: "달달한"
      synonyms: ["달달한", "달콤한", "달달", "달콤", "단"]
    - code: "sour"
      label: "새콤한"
      synonyms: ["새콤한", "시큼한", "신"]

  aroma:
    - code: "fruity"
      label: "과일향"
      synonyms: ["과일향", "과일맛"]
      children:
        - code: "citrus"
          label: "시트러스"
          synonyms: ["시트러스", "감귤"]
          children:
            - code: "lime"
              label: "라임"
              synonyms: ["라임"]
    - code: "smoky"
      label: "스모키"
      synonyms: ["스모키", "훈연향"]

  sensation:
    - code: "carbonated"
      label: "톡 쏘는"
      synonyms: ["톡 쏘는", "톡쏘는", "탄산"]
      note: "사용자 '톡 쏘는' 입력 시 carbonated/tingling 모두 후보"

  impression:
    - code: "refreshing"
      label: "상쾌한"
      note: "복합 인상: citrus + cooling + carbonation 조합으로 해석"
```

**설계 원칙 (객체지향)**
- 감각 축을 1급 개념으로 승격
- sweet, lime, smoky를 같은 타입으로 취급하지 않음
- 계층 구조로 상위/하위 개념 보존
- 원문 표현과 정규화된 개념 분리
- descriptor ontology 중심 설계

---

### Phase 4: 키워드 분석기 구현
**목표**: 사용자 입력에서 키워드 추출 (Descriptor 기반)

**테스트 작성 (TDD)**
- [ ] KeywordAnalyzer 단위 테스트
  - 맛 키워드 추출 테스트
  - 질감 키워드 추출 테스트
  - 자극감 키워드 추출 테스트
  - 향 키워드 추출 테스트
  - 분위기 키워드 추출 테스트
  - 복합 키워드 추출 테스트
  - 동의어 처리 테스트 ("달달한" = "달콤한")
  - 키워드 없는 경우 테스트

**구현**
- [ ] KeywordAnalyzer 서비스 구현
- [ ] 키워드 사전 정의
  - 맛 키워드 (달달한, 쓴, 새콤한 등)
  - 질감 키워드 (부드러운, 묵직한 등)
  - 자극감 키워드 (톡 쏘는, 강한 등)
  - 향 키워드 (과일향, 시트러스 등)
  - 분위기 키워드 (여름, 가볍게 등)
- [ ] 키워드 매칭 로직 구현
  - 문자열 포함 여부 체크
  - 동의어 처리
- [ ] 분석 결과 SensoryDescriptors 객체로 반환

**완료 조건**
- ✅ KeywordAnalyzer 구현 완료
- ✅ 모든 단위 테스트 통과
- ✅ 키워드 사전 정의 완료

**예상 산출물**
- KeywordAnalyzer.java
- KeywordAnalyzerTest.java
- 키워드 매핑 로직

**구현 방식**
```
입력: "달달하고 톡 쏘는 과일맛이 나는 부드러운 칵테일 추천해줘"

1. 키워드 사전과 비교
   - "달달하고" → TASTE: ["sweet"]
   - "톡 쏘는" → SENSATION: ["carbonated"]
   - "과일맛" → AROMA: ["fruity"]
   - "부드러운" → MOUTHFEEL: ["smooth"]

2. 결과 반환
   SensoryDescriptors {
     taste: ["sweet"],
     aroma: ["fruity"],
     mouthfeel: ["smooth"],
     sensation: ["carbonated"],
     impression: []
   }
```

---

### Phase 5: 칵테일 매칭 엔진 구현
**목표**: 분석된 키워드로 칵테일 추천

**테스트 작성 (TDD)**
- [ ] CocktailMatcher 단위 테스트
  - 단일 키워드 매칭 테스트
  - 복합 키워드 매칭 테스트
  - 매칭 점수 계산 테스트
  - 상위 N개 선정 테스트
  - 매칭되는 칵테일 없는 경우 테스트
  - 추천 이유 생성 테스트

**구현**
- [ ] CocktailMatcher 서비스 구현
- [ ] 매칭 점수 계산 로직
  - 각 키워드 카테고리별 가중치 적용
  - 태그 매칭 개수에 따라 점수 계산
- [ ] 상위 N개 칵테일 선정 (1~3개)
- [ ] 추천 이유 생성 로직
  - 매칭된 키워드 기반 설명 생성

**완료 조건**
- ✅ CocktailMatcher 구현 완료
- ✅ 모든 단위 테스트 통과
- ✅ 매칭 알고리즘 정상 동작

**예상 산출물**
- CocktailMatcher.java
- CocktailMatcherTest.java
- 매칭 알고리즘

**매칭 알고리즘**
```
1. 모든 칵테일에 대해 매칭 점수 계산
   - 각 카테고리별 일치하는 태그 개수 계산
   - 점수 = (taste 일치 * 0.3) + (texture 일치 * 0.2) +
           (carbonation 일치 * 0.2) + (flavor 일치 * 0.2) +
           (mood 일치 * 0.1)

2. 점수 기준 내림차순 정렬

3. 상위 1~3개 선정
   - 점수가 0인 경우 제외
   - 최소 1개 이상 반환

4. 추천 이유 생성
   - "달달한 맛과 톡 쏘는 느낌이 어울리는 칵테일입니다."
```

---

### Phase 6: 추천 서비스 구현
**목표**: 전체 추천 프로세스 통합

**테스트 작성 (TDD)**
- [ ] CocktailRecommendationService 테스트
  - 정상적인 추천 요청 테스트
  - 빈 문자열 입력 예외 테스트
  - null 입력 예외 테스트
  - 키워드 분석 결과 확인 테스트
  - 추천 결과 개수 확인 테스트 (1~3개)
  - 통합 플로우 테스트

**구현**
- [ ] CocktailRecommendationService 구현
- [ ] 추천 프로세스 구현
  1. 입력 검증
  2. 키워드 분석 (KeywordAnalyzer)
  3. 칵테일 매칭 (CocktailMatcher)
  4. 응답 생성
- [ ] 예외 처리
  - 빈 문자열 검증
  - null 체크

**완료 조건**
- ✅ CocktailRecommendationService 구현 완료
- ✅ 모든 서비스 레이어 테스트 통과
- ✅ 전체 추천 프로세스 정상 동작

**예상 산출물**
- CocktailRecommendationService.java
- CocktailRecommendationServiceTest.java
- 통합 비즈니스 로직

---

### Phase 7: API 컨트롤러 구현
**목표**: REST API 엔드포인트 제공

**테스트 작성 (TDD)**
- [ ] CocktailController 테스트
  - POST /api/v1/cocktails/recommend 성공 케이스
  - 잘못된 요청 400 에러 테스트
  - MockMvc를 이용한 통합 테스트
- [ ] HealthController 테스트
  - GET /api/v1/health 정상 응답 테스트

**구현**
- [ ] CocktailController 구현
  - POST /api/v1/cocktails/recommend
- [ ] HealthController 구현
  - GET /api/v1/health
- [ ] 요청 검증
  - @Valid 어노테이션 사용
- [ ] 응답 포맷 통일

**완료 조건**
- ✅ 컨트롤러 구현 완료
- ✅ 모든 컨트롤러 테스트 통과
- ✅ API 정상 동작

**예상 산출물**
- CocktailController.java
- HealthController.java
- CocktailControllerTest.java
- HealthControllerTest.java

---

### Phase 8: 예외 처리 구현
**목표**: 일관된 에러 응답 제공

**테스트 작성 (TDD)**
- [ ] GlobalExceptionHandler 테스트
  - InvalidRequestException 처리 테스트
  - 일반 Exception 처리 테스트
  - 에러 응답 포맷 확인 테스트
  - 타임스탬프 포함 확인 테스트

**구현**
- [ ] GlobalExceptionHandler 구현
  - @RestControllerAdvice 사용
- [ ] 커스텀 예외 정의
  - InvalidRequestException
- [ ] 에러 응답 포맷 통일
  - ErrorResponse 사용
  - 타임스탬프 포함

**완료 조건**
- ✅ GlobalExceptionHandler 구현 완료
- ✅ 모든 예외 처리 테스트 통과
- ✅ 일관된 에러 응답 제공

**예상 산출물**
- GlobalExceptionHandler.java
- InvalidRequestException.java
- GlobalExceptionHandlerTest.java

---

### Phase 9: End-to-End 테스트
**목표**: 전체 시스템 통합 검증

**테스트 작성**
- [ ] E2E 테스트 시나리오
  - 시나리오 1: 맛 키워드만 입력
    - "달달한 칵테일 추천해줘" → 추천 성공
  - 시나리오 2: 복합 키워드 입력
    - "달달하고 톡 쏘는 과일맛이 나는 부드러운 칵테일" → 추천 성공
  - 시나리오 3: 잘못된 입력
    - 빈 문자열 → 400 에러
    - null → 400 에러
  - 시나리오 4: Health Check
    - GET /api/v1/health → 200 응답

**테스트 방법**
- [ ] 실제 HTTP 요청/응답 테스트
- [ ] 전체 플로우 검증
- [ ] 응답 시간 확인
- [ ] 데이터 정합성 확인

**완료 조건**
- ✅ 모든 E2E 테스트 시나리오 통과
- ✅ 실제 환경에서 정상 동작 확인

**예상 산출물**
- E2ETest.java (또는 CocktailRecommendationE2ETest.java)
- E2E 테스트 시나리오 문서

---

### Phase 10: 문서화 및 마무리
**목표**: 프로젝트 문서화

**작업 내용**
- [ ] README.md 작성
  - 프로젝트 소개
  - 실행 방법
  - API 사용 예시
  - 테스트 실행 방법
- [ ] API 문서화
  - 엔드포인트 명세
  - 요청/응답 예시
- [ ] 코드 정리
  - 불필요한 주석 제거
  - 코드 포맷팅
  - TODO 제거

**완료 조건**
- ✅ README.md 작성 완료
- ✅ API 문서 작성 완료
- ✅ 코드 정리 완료

**예상 산출물**
- README.md
- API 문서
- 정리된 코드

---

## 3. 기술 스택 및 의존성

### 필수 의존성
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

### 선택 의존성 (향후 고려)
- Spring Data JPA (데이터베이스 연동 시)
- H2 Database (개발용)
- PostgreSQL (프로덕션용)

---

## 4. 구현 순서 요약

1. **Phase 1**: 프로젝트 기본 설정 → 애플리케이션 실행 테스트
2. **Phase 2**: DTO 및 도메인 모델 → DTO 테스트
3. **Phase 3**: 칵테일 데이터 준비 → Repository 테스트
4. **Phase 4**: 키워드 분석기 → KeywordAnalyzer 단위 테스트
5. **Phase 5**: 칵테일 매칭 엔진 → CocktailMatcher 단위 테스트
6. **Phase 6**: 추천 서비스 → Service 통합 테스트
7. **Phase 7**: API 컨트롤러 → Controller 통합 테스트
8. **Phase 8**: 예외 처리 → Exception Handler 테스트
9. **Phase 9**: E2E 테스트 → 전체 시스템 검증
10. **Phase 10**: 문서화 → README 및 API 문서

---

## 5. TDD 프로세스

각 Phase는 다음 순서로 진행됩니다:

```
1. 테스트 작성 (Red)
   - 구현하기 전에 테스트 먼저 작성
   - 실패하는 테스트 확인

2. 구현 (Green)
   - 테스트를 통과하는 최소한의 코드 작성
   - 테스트 통과 확인

3. 리팩토링 (Refactor)
   - 코드 개선
   - 테스트는 여전히 통과해야 함

4. 다음 Phase로 이동
```

---

## 6. 다음 단계

Phase 1부터 순차적으로 TDD 방식으로 구현을 시작합니다.
각 Phase의 테스트가 모두 통과해야 다음 Phase로 진행할 수 있습니다.
