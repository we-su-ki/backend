# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Project Name: cocktail-be
Group ID: com.cock
Package: com.cock.cocktail
Java Version: 21
Spring Boot Version: 3.5.0
Build Tool: Gradle

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

### Code Style
- 객체지향적으로 작성
- 지역변수 var 키워드로 선언하고, 네이밍으로 의미 전달
- 불변 객체 우선 (record, final 사용)
- 웹 계층 DTO: Java record 사용 (불변 객체)
- 도메인 모델: 비즈니스 로직에 따라 선택
  - 값 객체(Value Object): record 사용. equals/hashCode/toString은 record 기본 제공에 맡김
  - 엔티티/비즈니스 로직 포함: 상태 변경, 복잡한 비즈니스 로직 포함하면 일반 클래스 + Lombok
- 컬렉션 필드 초기화: 필드 선언 시점에 빈 컬렉션 할당 선호
- 사용하지 않는 메서드 제거: 코드베이스에서 참조되지 않는 메서드는 즉시 제거
  - 테스트에서만 사용되는 메서드도 프로덕션 코드에 포함하지 않음
- 정적 팩터리 최소화: 생성자와 파라미터가 동일한 정적 팩터리(`of`)는 사용하지 않음
- 계층 분리: JSON 직렬화/역직렬화는 웹 계층(DTO) 관심사
  - 도메인은 타 계층에 의존하지 않음: 단, JPA어노테이션 등은 편의성을 위해 예외
- 무분별 getter/setter 지양: 객체 간 메시지를 주고받는 형태로 구현
- Spring Bean 생성자: `@Service`, `@Component` 등 Spring Bean은 생성자를 직접 정의하지 않고 `@RequiredArgsConstructor` 사용
- 로거 주입: `@Slf4j` 애노테이션 사용.
- 주석 최소화: 알고리즘이 복잡하거나 도메인이 어려운 경우에만 주석 사용. 코드로 의도를 표현하는 것을 우선시
- 불필요한 중간 변수 제거: 변수에 담았다가 바로 리턴하는 경우, 직접 리턴
- 메서드 참조 선호: 람다식을 메서드 참조로 표현 가능하면 메서드 참조 사용
- RESTful API : HTTP 메서드와 상태 코드를 의미에 맞게 사용
- 유틸리티 클래스로 중복 제거: 중복되는 표준 타입 검증은 유틸리티 클래스로 추출
- 중복 클래스 제거: 동일한 구조의 클래스가 여러 곳에 정의되어 있으면 하나로 통합
- 공통 클래스 패키지 배치: 여러 하위 패키지에서 사용하는 공통 클래스는 상위 패키지에 배치
- 중복 초기화 로직 제거: 동일한 리소스를 여러 곳에서 각각 로드하는 경우, 싱글톤 관리
- 데이터와 로직의 응집도: 데이터를 가진 객체에 해당 데이터를 활용하는 로직을 배치
- 의미 있는 네이밍: 클래스의 역할이 변경되면 이름도 함께 변경
- 패키지 구조: 도메인과 인프라를 명확히 분리
- 테스트 위치: 프로덕션 코드와 동일한 패키지 구조 유지, 책임이 이동하면 테스트도 함께 이동
- 메서드 접근 제어 및 배치:
  - 클래스 내부에서만 사용하는 메서드는 private으로 선언
  - public 메서드를 먼저 배치하고, 그 아래 private 메서드 배치
  - private 메서드를 직접 테스트하지 않음 (블랙박스 테스트 선호)
- Repository 메서드 설계:
  - 불필요한 default 메서드 지양. 단순 null/empty 체크만 하는 wrapper는 제거
  - JPQL 쿼리 메서드를 직접 제공하고, 필요한 검증은 호출하는 쪽에서 처리
  - 최소한의 Repository 상속: Spring Data JPA가 제공하는 Repository 인터페이스 중 필요한 기능의 최소한만 상속

### Test Conventions
- TDD 방식: 테스트 먼저 작성해서 완료 조건으로 이용
- 프로덕션 코드 변경 시 테스트 동기화: 프로덕션 코드를 변경하면 항상 테스트 코드도 그에 맞게 즉시 변경
  - 패키지 이동, 클래스명 변경, 메서드 시그니처 변경 등 모든 변경사항 반영
  - 테스트가 깨지면 즉시 수정하여 항상 통과하는 상태 유지
- 객체 생성: 도메인 객체 생성 시 setter 대신 빌더 또는 생성자 우선
- Assertion 그룹화: 여러 개의 assertThat은 assertAll로 감싸기
- 표현력 있는 Assertion: AssertJ의 체이닝과 메서드 참조를 활용하여 간결하고 의도가 명확하게 작성
- 외부 인프라 / 구현 기술 테스트: 대역(mock)을 사용한 단위 테스트로 작성
- JSON 직렬화 테스트: 커스텀 JsonSerializer/JsonDeserializer 작성한 경우에만 테스트
- 실용적인 테스트 작성: 핵심 비즈니스 로직 테스트에 집중
- 테스트는 간결하고 이해하기 쉽게 작성: given/when/then이 명확하고 간결하게 드러나도록 노력

### Git Commit Message
- 한글로 간결하게 핵심만 작성
- Claude Code 관련 문구 제외
- 커밋 타이밍: 사용자가 명시적으로 요청할 때만 커밋, 적절한 시점이라 생각하면 사용자에게 제안

## 의사결정 원칙

- 모호한 요구사항은 질문: 구현 방향이나 의도가 불명확하면 사용자에게 확인
- 실용주의 우선: 과도한 엔지니어링보다 단순하고 명확한 구현 선호
- 최소한의 필요충분한 기능: 요구사항에 없는 기능은 추가하지 않음

## References

- 상세 기능 명세: `SPEC.md`
- 구현 계획: `PLAN.md`
- 제품 요구사항: `PRD.md`
- 코드는 객체지향적으로 작성해. 객체지향 생활 체조를 잘 준수하고, OOP의 특성을 살려서 작성해.
