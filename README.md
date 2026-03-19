# 칵테일 레시피 추천 서비스

사용자가 자연어로 원하는 맛이나 분위기를 입력하면 적합한 칵테일 레시피를 AI가 추천해주는 서비스

## 기술 스택

- **Java**: 21
- **Spring Boot**: 3.5.0
- **Spring Data JPA**: H2 Database
- **Build Tool**: Gradle
- **Test**: JUnit 5, MockMvc

## 프로젝트 구조

```
src/main/java/com/cock/cocktail/
├── web/                            # 웹 계층
│   ├── CocktailController.java
│   └── dto/
├── service/                        # 서비스 인터페이스
│   ├── KeywordAnalyzer.java
│   ├── CocktailMatcher.java
│   └── CocktailRecommendationService.java
├── infrastructure/                 # 구현체
│   ├── config_based_analyzer/
│   ├── matching/
│   └── recommendation/
├── domain/                         # 도메인 모델
│   ├── Cocktail.java
│   ├── SensoryDescriptors.java
│   └── MatchedCocktail.java
├── repository/
│   └── CocktailRepository.java
└── exception/
    └── GlobalExceptionHandler.java
```

## 시작하기

### 1. 프로젝트 클론

```bash
git clone <repository-url>
cd cocktail-be
```

### 2. 빌드

```bash
./gradlew build
```

### 3. 실행

```bash
./gradlew bootRun
```

서버가 `http://localhost:8080`에서 시작됩니다.

## API 사용 예시

### 1. 키워드 분석

사용자 입력에서 감각 디스크립터를 추출합니다.

```bash
curl "http://localhost:8080/api/v1/cocktails/analyze?query=달달한%20칵테일"
```

**응답:**
```json
{
  "taste": ["sweet"],
  "aroma": [],
  "mouthfeel": [],
  "sensation": [],
  "impression": []
}
```

### 2. 칵테일 추천

사용자 쿼리 기반으로 칵테일을 추천합니다.

```bash
curl "http://localhost:8080/api/v1/cocktails/recommend?query=달달하고%20과일향%20나는%20칵테일"
```

**응답:**
```json
{
  "cocktails": [
    {
      "id": 3,
      "name": "피나콜라다",
      "ingredients": [
        {"name": "화이트 럼", "amount": "50ml"},
        {"name": "파인애플 주스", "amount": "80ml"},
        {"name": "코코넛 크림", "amount": "30ml"}
      ],
      "recipe": "블렌더에 모든 재료와 얼음을 넣는다\n부드럽게 갈아준다\n글라스에 따르고 파인애플로 장식한다",
      "score": 1.0,
      "reason": "달달한, 과일향 특징을 가진 칵테일입니다.",
      "matchedKeywords": ["fruity", "sweet"]
    }
  ],
  "count": 1
}
```

## 테스트 실행

### 전체 테스트

```bash
./gradlew test
```

### 특정 테스트 클래스 실행

```bash
./gradlew test --tests CocktailControllerTest
./gradlew test --tests DataIntegrityTest
./gradlew test --tests EdgeCaseE2ETest
```

### 테스트 통계

- **총 테스트**: 163개
- **단위 테스트**: 110개
- **통합 테스트**: 53개
- **통과율**: 100%

## API 엔드포인트

자세한 API 명세는 [API.md](API.md)를 참조하세요.

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/v1/cocktails/analyze` | 키워드 분석 |
| GET | `/api/v1/cocktails/recommend` | 칵테일 추천 |

## 주요 기능

### 1. 자연어 키워드 분석
- 사용자 입력에서 감각 디스크립터 추출
- 5가지 감각 축: taste, aroma, mouthfeel, sensation, impression
- 동의어 처리 지원

### 2. 칵테일 매칭 엔진
- Recall 기반 점수 계산: `score = |쿼리 ∩ 칵테일| / |쿼리|`
- 최대 3개 칵테일 추천
- 점수 내림차순 정렬

### 3. 추천 이유 생성
- 매칭된 키워드 기반 자연어 설명 생성
- 예: "달달한, 과일향 특징을 가진 칵테일입니다."

## 성능

- 서버 시작 시간: ~2초
- API 평균 응답 시간: 6ms
- 첫 요청 응답 시간: 47ms (워밍업)

## 개발 원칙

- **TDD (Test-Driven Development)**: 테스트 먼저 작성
- **객체지향 설계**: 객체지향 생활 체조 준수
- **Hexagonal Architecture**: 인터페이스 기반 설계
- **불변성**: record 타입 적극 활용
- **코드 품질**: 불필요한 주석 제거, 코드로 표현

## 라이센스

MIT License
