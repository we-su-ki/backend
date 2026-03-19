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
- **정적 팩터리 최소화**: 생성자와 파라미터가 동일한 정적 팩터리(`of`)는 사용하지 않음
- **계층 분리**: JSON 직렬화/역직렬화는 웹 계층(DTO) 관심사
- **무분별 getter/setter 지양**: 객체 간 메시지를 주고받는 형태로 구현
- **Spring Bean 생성자**: `@Service`, `@Component` 등 Spring Bean은 생성자를 직접 정의하지 않고 `@RequiredArgsConstructor` 사용
- **주석 최소화**:
  - 알고리즘이 복잡하거나 도메인이 어려운 경우에만 주석 사용
  - 자명한 코드에는 JavaDoc, 메서드 주석 불필요
  - given/when/then 같은 테스트 구조 주석 불필요
  - 코드로 의도를 표현하는 것을 우선시
- **메서드 참조 선호**: 람다식을 메서드 참조로 표현 가능하면 메서드 참조 사용
  - 예: `error -> error.getDefaultMessage()` → `FieldError::getDefaultMessage`
- **RESTful API 원칙**:
  - idempotent한 조회 작업은 GET 사용
  - POST는 리소스 생성이나 상태 변경에만 사용
  - 쿼리 파라미터로 검색 조건 전달 (GET)
- **로깅 원칙**:
  - 500대 에러는 ERROR 레벨로 로깅
  - 스택 트레이스 포함하여 디버깅 용이하게

### Test Conventions
- **TDD 방식**: 테스트 먼저 작성해서 완료 조건으로 이용
- **객체 생성**: 도메인 객체 생성 시 setter 대신 빌더 또는 생성자 사용
- **Assertion 그룹화**: 여러 개의 assertThat은 assertAll로 감싸서 모든 assertion 실행 보장
- **infrastructure 테스트**:
  - SpringBootTest 사용 금지
  - 대역(mock)을 사용한 단위 테스트로 작성
  - 의존성 주입이 필요한 경우 생성자 주입 + mock 객체 활용
- **JSON 직렬화 테스트**:
  - Spring Boot 기본 Jackson 사용 시 JSON 테스트 불필요
  - 커스텀 JsonSerializer/JsonDeserializer 작성한 경우에만 테스트
- **실용적인 테스트 작성**:
  - 핵심 비즈니스 로직 테스트에 집중
  - 헬스체크, 성능 벤치마크, 로깅 검증 등은 불필요
  - 테스트는 실제로 가치를 제공하는 경우에만 작성

### Git Commit Message
- 한글로 간결하게 핵심만 작성
- 형식: `{작업 내용}` (예: "DTO를 record로 변경")
- Claude Code 관련 문구 제외
- **커밋 타이밍**: 사용자가 명시적으로 요청할 때만 커밋, 작업 완료 후 자동 커밋 금지

## 문서화 원칙

- **README.md**: 프로젝트 소개, 실행 방법, API 사용 예시 포함
- **API.md**: 상세한 엔드포인트 명세, 요청/응답 예시, 에러 코드
- **PHASE*_TASKS.md**: 각 Phase별 작업 내역과 완료 현황 기록
- **문서는 실용적으로**: 개발자가 실제로 필요한 정보만 포함

## 의사결정 원칙

- **모호한 요구사항은 질문**: 구현 방향이 불명확하면 사용자에게 확인
- **실용주의 우선**: 과도한 엔지니어링보다 단순하고 명확한 구현 선호
- **RESTful 원칙 준수**: HTTP 메서드와 상태 코드를 의미에 맞게 사용
- **최소한의 필요충분한 기능**: 요구사항에 없는 기능은 추가하지 않음

## References

- 상세 기능 명세: `SPEC.md`
- 구현 계획: `PLAN.md`
- 제품 요구사항: `PRD.md`
- 코드는 객체지향적으로 작성해. 객체지향 생활 체조를 잘 준수하고, OOP의 특성을 살려서 작성해.
