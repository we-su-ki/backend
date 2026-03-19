# Phase 5: 칵테일 매칭 엔진 구현 - 세부 Task

## 목표
분석된 SensoryDescriptors를 기반으로 DB에서 칵테일을 검색하고 매칭 점수를 계산하여 추천

---

## Task 1: CocktailMatcher 서비스 인터페이스 설계

### 목표
매칭 엔진의 인터페이스 정의

### 작업 내용
- [ ] `CocktailMatcher` 인터페이스 작성
  - `List<MatchedCocktail> match(SensoryDescriptors descriptors)`
- [ ] `MatchedCocktail` 도메인 모델 작성
  - `Cocktail cocktail`
  - `double score` (0.0 ~ 1.0)
  - `String reason` (추천 이유)
  - `Set<DescriptorCode> matchedDescriptors` (매칭된 descriptor 목록)

### 완료 조건
- CocktailMatcher.java 인터페이스 작성
- MatchedCocktail.java 도메인 모델 작성
- 컴파일 성공

### 산출물
- `src/main/java/com/cock/cocktail/service/CocktailMatcher.java`
- `src/main/java/com/cock/cocktail/domain/MatchedCocktail.java`

---

## Task 2: CocktailRepository에 descriptor 기반 검색 추가

### 목표
SensoryDescriptors로 칵테일 검색 기능 구현

### 작업 내용
- [ ] CocktailRepository에 메서드 추가
  - `List<Cocktail> findByDescriptors(Set<DescriptorCode> descriptors)`
  - JPQL 또는 QueryDSL로 descriptor 매칭 쿼리 작성
- [ ] 테스트 작성
  - descriptor로 칵테일 검색 테스트
  - 여러 descriptor 조합 검색 테스트
  - 매칭되는 칵테일 없는 경우 테스트

### 완료 조건
- CocktailRepository 검색 메서드 구현
- 모든 Repository 테스트 통과

### 산출물
- `CocktailRepository.java` (수정)
- `CocktailRepositoryTest.java` (테스트 추가)

### 기술적 고려사항
- DescriptorCode 매칭: axis와 value 모두 일치해야 함
- OR 조건: 하나라도 매칭되면 후보에 포함
- 성능: N+1 문제 주의 (fetch join 고려)

---

## Task 3: 매칭 점수 계산 로직 구현 (TDD)

### 목표
칵테일과 SensoryDescriptors 간 유사도를 점수화

### 테스트 작성
- [ ] MatchScoreCalculatorTest 작성
  - 완전 일치 시 1.0 테스트
  - 부분 일치 시 점수 계산 테스트
  - 일치하는 descriptor 없으면 0.0 테스트
  - 축별 가중치 적용 테스트
  - 교집합 비율 계산 테스트

### 구현
- [ ] `MatchScoreCalculator` 유틸리티 클래스 작성
  - `double calculate(Cocktail cocktail, SensoryDescriptors query)`
  - 매칭 알고리즘:
    ```
    score = 교집합 개수 / 쿼리 descriptor 개수

    예시:
    쿼리: [sweet, fruity, smooth]
    칵테일: [sweet, fruity, carbonated, summer]
    교집합: [sweet, fruity] = 2개
    score = 2 / 3 = 0.67
    ```

### 완료 조건
- MatchScoreCalculator.java 구현
- 모든 단위 테스트 통과
- 점수 계산 로직 검증 완료

### 산출물
- `src/main/java/com/cock/cocktail/infrastructure/matching/MatchScoreCalculator.java`
- `src/test/java/com/cock/cocktail/infrastructure/matching/MatchScoreCalculatorTest.java`

### 설계 결정 사항 (리뷰 필요)
**Q1. 축별 가중치를 적용할까요?**
- 옵션 A: 동일 가중치 (모든 descriptor 동등)
- 옵션 B: 축별 가중치 (TASTE 30%, AROMA 25%, MOUTHFEEL 20%, SENSATION 15%, IMPRESSION 10%)
- **제안**: 옵션 A로 시작 (단순성), 나중에 옵션 B로 개선 가능

**Q2. 점수 계산 방식은?**
- 옵션 A: Jaccard 유사도 = |A ∩ B| / |A ∪ B|
- 옵션 B: 재현율 기반 = |A ∩ B| / |A| (쿼리 기준)
- **제안**: 옵션 B (사용자가 원하는 특성을 얼마나 만족하는가)

---

## Task 4: 추천 이유 생성 로직 구현 (TDD)

### 목표
매칭된 descriptor를 기반으로 자연어 추천 이유 생성

### 테스트 작성
- [ ] ReasonGeneratorTest 작성
  - 단일 descriptor 매칭 시 이유 생성 테스트
  - 복수 descriptor 매칭 시 이유 생성 테스트
  - 축별 descriptor 조합 테스트
  - 빈 매칭 시 기본 이유 테스트

### 구현
- [ ] `ReasonGenerator` 유틸리티 클래스 작성
  - `String generate(Set<DescriptorCode> matchedDescriptors)`
  - 로직:
    ```
    매칭된 descriptor의 label을 조합

    예시:
    matchedDescriptors: [
      DescriptorCode(TASTE, "sweet"),
      DescriptorCode(AROMA, "fruity")
    ]

    → "달달한 맛과 과일향이 어울리는 칵테일입니다."
    ```

### 완료 조건
- ReasonGenerator.java 구현
- 모든 단위 테스트 통과
- 자연스러운 문장 생성 검증

### 산출물
- `src/main/java/com/cock/cocktail/infrastructure/matching/ReasonGenerator.java`
- `src/test/java/com/cock/cocktail/infrastructure/matching/ReasonGeneratorTest.java`

### 설계 결정 사항 (리뷰 필요)
**Q3. Descriptor의 한글 label은 어디서 가져올까요?**
- 옵션 A: keywords.yml에서 code → label 매핑 읽기
- 옵션 B: DescriptorCode에 label 필드 추가
- 옵션 C: 별도 DescriptorLabel 저장소 생성
- **제안**: 옵션 A (이미 keywords.yml에 label 정의되어 있음)

---

## Task 5: CocktailMatcher 구현체 작성 (TDD)

### 목표
전체 매칭 프로세스를 조율하는 서비스 구현

### 테스트 작성
- [ ] SimpleCocktailMatcherTest 작성
  - 단일 descriptor 매칭 테스트
  - 복수 descriptor 매칭 테스트
  - 상위 N개 선정 테스트 (최대 3개)
  - 매칭되는 칵테일 없으면 빈 리스트 테스트
  - 점수 내림차순 정렬 테스트
  - 점수 0인 칵테일 제외 테스트

### 구현
- [ ] `SimpleCocktailMatcher` 구현
  - CocktailRepository, MatchScoreCalculator, ReasonGenerator 주입
  - 매칭 프로세스:
    ```
    1. Repository에서 descriptor 기반 후보 칵테일 조회
    2. 각 칵테일에 대해 점수 계산
    3. 점수 내림차순 정렬
    4. 상위 3개 선정 (점수 > 0)
    5. 추천 이유 생성
    6. MatchedCocktail 리스트 반환
    ```

### 완료 조건
- SimpleCocktailMatcher.java 구현
- @Service 등록
- 모든 단위 테스트 통과
- 매칭 프로세스 정상 동작

### 산출물
- `src/main/java/com/cock/cocktail/infrastructure/matching/SimpleCocktailMatcher.java`
- `src/test/java/com/cock/cocktail/infrastructure/matching/SimpleCocktailMatcherTest.java`

---

## Task 6: 통합 테스트 작성

### 목표
KeywordAnalyzer → CocktailMatcher 전체 플로우 검증

### 테스트 작성
- [ ] CocktailMatchingIntegrationTest 작성
  - "달달하고 과일향 나는 칵테일" → 매칭 성공 테스트
  - "부드럽고 톡 쏘는 칵테일" → 매칭 성공 테스트
  - "존재하지 않는 키워드" → 빈 리스트 테스트
  - 매칭 점수 정렬 확인 테스트
  - 추천 이유 생성 확인 테스트

### 완료 조건
- 모든 통합 테스트 통과
- 전체 매칭 플로우 정상 동작

### 산출물
- `src/test/java/com/cock/cocktail/CocktailMatchingIntegrationTest.java`

---

## Task 7: 성능 및 품질 검증

### 작업 내용
- [ ] 매칭 성능 측정
  - 100개 칵테일 대상 응답 시간 < 100ms
- [ ] 엣지 케이스 테스트
  - DB에 칵테일 없는 경우
  - 모든 칵테일 점수 0인 경우
  - 쿼리 descriptor가 빈 경우
- [ ] 코드 리뷰
  - 객체지향 원칙 준수 확인
  - 불필요한 코드 제거
  - 변수명/메서드명 명확성 확인

### 완료 조건
- 성능 기준 충족
- 모든 엣지 케이스 처리
- 코드 품질 기준 통과

---

## 전체 완료 조건

- [ ] 모든 Task의 테스트 통과
- [ ] CocktailMatcher 인터페이스 구현 완료
- [ ] 매칭 알고리즘 정상 동작
- [ ] 추천 이유 생성 정상 동작
- [ ] 통합 테스트 통과
- [ ] 성능 및 품질 검증 완료

---

## 리뷰 포인트

### 설계 결정 사항
1. **축별 가중치**: 동일 vs 차등 (Task 3)
2. **점수 계산 방식**: Jaccard vs 재현율 (Task 3)
3. **Label 조회 방식**: keywords.yml vs DescriptorCode 필드 (Task 4)

### 기술적 고려사항
1. **Repository 쿼리**: fetch join으로 N+1 문제 해결 필요 (Task 2)
2. **최대 추천 개수**: 3개 고정 vs 설정 가능 (Task 5)
3. **점수 임계값**: 0 초과 vs 특정 값 이상 (Task 5)

### 패키지 구조
```
infrastructure/
└── matching/
    ├── SimpleCocktailMatcher.java
    ├── MatchScoreCalculator.java
    └── ReasonGenerator.java
```

이 구조가 적절한가요? 아니면 service/ 하위에 배치해야 할까요?
