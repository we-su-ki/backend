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
├── service/                # 서비스 인터페이스 (예: KeywordAnalyzer)
├── repository/             # 데이터 접근 계층
│   └── *Repository.java    # 데이터 접근
├── domain/                 # 도메인 모델
│   ├── *.java              # 도메인 엔티티
├── infrastructure/
│   └── config_based_analyzer/  # 설정 기반 분석기 구현체
├── exception/              # 예외 처리
│   ├── *Exception.java     # 커스텀 예외
│   └── GlobalExceptionHandler.java
└── config/                 # 설정
```

### 테스트 패키지 규칙
- `KeywordAnalyzerTest`는 `src/test/java/com/cock/cocktail/application` 패키지에 위치
- 인프라 구현 테스트는 `src/test/java/com/cock/cocktail/infrastructure/config_based_analyzer` 사용
- 도메인 테스트는 `src/test/java/com/cock/cocktail/domain` 사용

## Coding Conventions

### 구현 규칙
- **웹 계층 DTO**: Java record 사용 (불변 객체)
- **도메인 모델**: 비즈니스 로직에 따라 선택
  - 단순 값 객체: record 사용 (예: `Ingredient`)
  - 비즈니스 로직 필요: 일반 클래스 + Lombok (예: `Cocktail`)

### Code Style
- 불변 객체 우선 (record, final 사용)
- **컬렉션 필드 초기화**: null 체크 대신 필드 선언 시점에 빈 컬렉션 할당
- **사용하지 않는 메서드 제거**: 코드베이스에서 참조되지 않는 메서드는 즉시 제거
- **정적 팩터리 최소화**:
  - 생성자와 파라미터가 동일한 정적 팩터리(`of`)는 사용하지 않음
  - 생성자를 직접 사용
- **계층 분리**: JSON 직렬화/역직렬화는 웹 계층(DTO) 관심사
  - 도메인 엔티티는 순수한 비즈니스 로직만 포함
  - `@JsonProperty` 등의 직렬화 어노테이션은 도메인에 사용 금지

### Database & Data Initialization
- **데이터 초기화**: SQL 파일(`data.sql`) 사용, CommandLineRunner/DataLoader 사용 금지
- **데이터베이스**: H2 파일 모드 사용
- **JPA**: Spring Data JPA 사용, repository는 인터페이스로 작성
- **감각 descriptor 저장 규칙**:
  - 컬렉션 테이블: `cocktail_sensory_descriptors`
  - 컬럼: `cocktail_id`, `axis`, `"value"`
  - `axis`는 `SensoryAxis` enum 문자열 저장
  - `"value"`는 `keywords.yml`의 `label`이 아닌 `code` 저장
    - 예: `달달한` X, `sweet` O
    - 예: `과일향` X, `fruity` O

### Domain Modeling Rules
- `CocktailTag`는 사용하지 않음
- 칵테일의 감각 정보는 `Cocktail.sensoryDescriptors: List<DescriptorCode>`로 표현
- `Cocktail#getSensoryDescriptors()`는 `new SensoryDescriptors(sensoryDescriptors)` 형태 유지
- `SensoryDescriptors`는 `List<DescriptorCode>`를 받는 생성자에서 축별 그룹핑/정규화 수행
- `DescriptorCode`는 `@Embeddable` 값 객체로 유지하며, `axis` + `"value"` 조합 사용

### Repository Rules
- 감각 검색 쿼리는 `sensoryDescriptors`를 기준으로 작성
- `findByTag(category, tag)` 호환 메서드를 유지하되 내부적으로 `SensoryAxis`로 매핑
- 신규 로직은 가능하면 `findBySensoryDescriptor(SensoryAxis axis, String value)` 중심으로 작성

### Test Conventions
- **TDD 방식**: 테스트 먼저 작성해서 완료 조건으로 이용
- 도메인/리포지토리 테스트는 code 기반 값(`sweet`, `fruity` 등)으로 검증
- 감각 관련 테스트에서 label 한글 문자열 검증 금지 (DB 저장 기준은 code)

### Git Commit Message
- 한글로 간결하게 핵심만 작성
- 형식: `{작업 내용}` (예: "DTO를 record로 변경")
- Claude Code 관련 문구 제외
- **커밋 타이밍**: 사용자가 명시적으로 요청할 때만 커밋, 작업 완료 후 자동 커밋 금지

## References

- 상세 기능 명세: `SPEC.md`
- 구현 계획: `PLAN.md`
- 제품 요구사항: `PRD.md`
- 코드는 객체지향적으로 작성해. 객체지향 생활 체조를 잘 준수하고, OOP의 특성을 살려서 작성해.
