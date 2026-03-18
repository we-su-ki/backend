# Claude Code Instructions

This file contains instructions and context for Claude Code when working on the cocktail-be project.

## Project Overview

**Project Name**: cocktail-be
**Group ID**: com.cock
**Package**: com.cock.cocktail
**Java Version**: 21
**Spring Boot Version**: 3.5.0
**Build Tool**: Gradle

## Project Description

칵테일 레시피 추천 서비스 - 사용자가 자연어로 원하는 맛이나 분위기를 입력하면 적합한 칵테일 레시피를 AI가 추천해주는 서비스

## Package Structure

```
com.cock.cocktail
├── web/                    # 웹 계층
│   ├── *Controller.java    # REST API 컨트롤러
│   └── dto/                # 웹 요청/응답 DTO (record)
├── service/                # 비즈니스 로직
├── domain/                 # 도메인 모델
│   ├── *.java              # 도메인 엔티티
│   └── *Repository.java    # 데이터 접근
├── exception/              # 예외 처리
│   ├── *Exception.java     # 커스텀 예외
│   └── GlobalExceptionHandler.java
└── config/                 # 설정
```

## Coding Conventions

### 1. Package 규칙
- `web/`: 웹 계층 (Controller, DTO)
  - Controller는 `web/` 직접 하위에 위치
  - DTO는 `web/dto/` 하위에 위치
- `service/`: 비즈니스 로직
- `domain/`: 도메인 모델 및 Repository
- `exception/`: 예외 처리 관련 클래스
- `config/`: Spring 설정 클래스

### 2. DTO 구현 규칙
- **웹 계층 DTO**: Java record 사용 (불변 객체)
  - `CocktailRecommendationRequest`
  - `CocktailRecommendationResponse`
  - `CocktailDto`
  - `AnalyzedKeywords`
  - `ErrorResponse`
- **도메인 모델**: 비즈니스 로직에 따라 선택
  - 단순 값 객체: record 사용 (예: `Ingredient`)
  - 비즈니스 로직 필요: 일반 클래스 + Lombok (예: `Cocktail`)

### 3. Naming Conventions
- **Controller**: `*Controller` (예: `CocktailController`)
- **Service**: `*Service` (예: `CocktailRecommendationService`)
- **Repository**: `*Repository` (예: `CocktailRepository`)
- **DTO**: `*Request`, `*Response`, `*Dto`
- **Exception**: `*Exception`

### 4. Code Style
- Java 21 features 사용 (record, pattern matching 등)
- Lombok 사용 최소화 (record 우선)
- 불변 객체 우선 (record, final 사용)
- 명확한 변수명/메서드명 사용

### 5. Spring Boot Conventions
- 레이어 구조: Web → Service → Domain
- 의존성 주입: 생성자 주입 사용
- REST API: RESTful 설계 원칙 준수
- Validation: Bean Validation 사용 (`@NotBlank`, `@Valid` 등)

### 6. Test Conventions
- **TDD 방식**: 테스트 먼저 작성 (Red → Green → Refactor)
- 테스트 패키지 구조는 main과 동일하게 유지
- Given-When-Then 패턴 사용
- `@DisplayName`으로 테스트 의도 명확히 표현

### 7. Git Commit Message
- 한글로 간결하게 핵심만 작성
- 형식: `{작업 내용}` (예: "DTO를 record로 변경")
- Claude Code 관련 문구 제외

## Development Guidelines

### API 설계
- Base URL: `/api/v1`
- RESTful 원칙 준수
- 명확한 HTTP 메서드 사용 (GET, POST, PUT, DELETE)

### 에러 처리
- 일관된 에러 응답 형식 (`ErrorResponse`)
- 적절한 HTTP 상태 코드 사용
- GlobalExceptionHandler로 중앙 집중식 예외 처리

### 보안
- 입력 검증: Bean Validation 사용
- 민감 정보 로깅 금지
- CORS 설정 관리

## Project Structure

```
src/
├── main/
│   ├── java/com/cock/cocktail/
│   │   ├── CocktailBeApplication.java
│   │   ├── web/
│   │   │   ├── CocktailController.java
│   │   │   ├── HealthController.java
│   │   │   └── dto/
│   │   │       ├── CocktailRecommendationRequest.java (record)
│   │   │       ├── CocktailRecommendationResponse.java (record)
│   │   │       ├── CocktailDto.java (record)
│   │   │       ├── AnalyzedKeywords.java (record)
│   │   │       └── ErrorResponse.java (record)
│   │   ├── service/
│   │   │   ├── CocktailRecommendationService.java
│   │   │   ├── KeywordAnalyzer.java
│   │   │   └── CocktailMatcher.java
│   │   ├── domain/
│   │   │   ├── Cocktail.java
│   │   │   ├── Ingredient.java (record)
│   │   │   └── CocktailRepository.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── InvalidRequestException.java
│   │   └── config/
│   │       └── WebConfig.java
│   └── resources/
│       ├── application.properties
│       └── data/
│           └── cocktails.json
└── test/
    └── java/com/cock/cocktail/
        ├── web/
        ├── service/
        └── CocktailBeApplicationTests.java
```

## Build and Run

### Build the project
```bash
./gradlew build
```

### Run the application
```bash
./gradlew bootRun
```

### Run tests
```bash
./gradlew test
```

### Run specific test
```bash
./gradlew test --tests "com.cock.cocktail.web.*"
```

## Notes for Claude Code

### 개발 시 주의사항
- 항상 기존 코드를 먼저 읽고 이해한 후 수정
- 패키지 구조와 명명 규칙 준수
- TDD 방식으로 개발 (테스트 → 구현 → 리팩토링)
- 변경 사항은 반드시 테스트로 검증
- 커밋 메시지는 한글로 간결하게

### Phase별 진행 상황
- ✅ Phase 1: 프로젝트 기본 설정
- ✅ Phase 2: DTO 및 도메인 모델 구현
- ⏳ Phase 3: 칵테일 데이터 준비
- ⏳ Phase 4: 키워드 분석기 구현
- ⏳ Phase 5: 칵테일 매칭 엔진 구현
- ⏳ Phase 6: 추천 서비스 구현
- ⏳ Phase 7: API 컨트롤러 구현
- ⏳ Phase 8: 예외 처리 구현
- ⏳ Phase 9: E2E 테스트
- ⏳ Phase 10: 문서화

## References

- 상세 기능 명세: `SPEC.md`
- 구현 계획: `PLAN.md`
- 제품 요구사항: `PRD.md`
