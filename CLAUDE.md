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
- **정적 팩터리 최소화**: - 생성자와 파라미터가 동일한 정적 팩터리(`of`)는 사용하지 않음
- **계층 분리**: JSON 직렬화/역직렬화는 웹 계층(DTO) 관심사
- **무분별 getter/setter 지양**: 객체 간 메시지를 주고받는 형태로 구현

### Domain Modeling Rules
- 칵테일의 감각 정보는 `Cocktail.sensoryDescriptors: List<DescriptorCode>`로 표현
- `Cocktail#getSensoryDescriptors()`는 `new SensoryDescriptors(sensoryDescriptors)` 형태 유지
- `SensoryDescriptors`는 `List<DescriptorCode>`를 받는 생성자에서 축별 그룹핑/정규화 수행
- `DescriptorCode`는 `@Embeddable` 값 객체로 유지하며, `axis` + `"value"` 조합 사용

### Test Conventions
- **TDD 방식**: 테스트 먼저 작성해서 완료 조건으로 이용
- **객체 생성**: 도메인 객체 생성 시 setter 대신 빌더 또는 생성자 사용
- **Assertion 그룹화**: 여러 개의 assertThat은 assertAll로 감싸서 모든 assertion 실행 보장

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
