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
├── repository/             # 데이터 접근 계층
│   └── *Repository.java    # 데이터 접근
├── domain/                 # 도메인 모델
│   ├── *.java              # 도메인 엔티티
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

### Test Conventions
- **TDD 방식**: 테스트 먼저 작성해서 완료 조건으로 이용

### Git Commit Message
- 한글로 간결하게 핵심만 작성
- 형식: `{작업 내용}` (예: "DTO를 record로 변경")
- Claude Code 관련 문구 제외

## References

- 상세 기능 명세: `SPEC.md`
- 구현 계획: `PLAN.md`
- 제품 요구사항: `PRD.md`
