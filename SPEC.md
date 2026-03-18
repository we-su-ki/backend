# 칵테일 레시피 추천 서비스 기능 명세서

## 1. 시스템 개요

### 1.1 기술 스택
- **Backend**: Spring Boot 3.5.0, Java 21
- **Build Tool**: Gradle
- **추천 모델**: 자체 개발 칵테일 추천 알고리즘

### 1.2 시스템 구조
```
Client
   ↓
REST API (Web Layer)
   ↓
비즈니스 로직 (Service)
   ├── 키워드 분석기
   └── 칵테일 추천 모델
   ↓
도메인 모델 (Domain)
```

### 1.3 패키지 구조
```
com.cock.cocktail
├── web/              # 웹 계층 (Controller, DTO)
│   └── dto/          # 웹 요청/응답 DTO (record)
├── service/          # 비즈니스 로직
├── domain/           # 도메인 모델 (Cocktail, Ingredient)
├── exception/        # 예외 처리
└── config/           # 설정
```

**구현 원칙**
- 웹 계층 DTO는 record로 구현
- 도메인 모델은 일반 클래스로 구현 (Lombok 사용)

## 2. API 명세

### 2.1 칵테일 추천 요청

**Endpoint**
```
POST /api/v1/cocktails/recommend
```

**Request**
```json
{
  "query": "달달하고 톡 쏘는 과일맛이 나는 부드러운 칵테일 추천해줘"
}
```

**Request 필드**

| 필드    | 타입     | 필수 | 설명                        |
|-------|--------|----|---------------------------|
| query | String | O  | 사용자가 입력한 자연어 문장 (최대 500자) |

**Response (Success - 200)**
```json
{
  "recommendations": [
    {
      "id": "cocktail-001",
      "name": "피치 스파클",
      "ingredients": [
        {
          "name": "보드카",
          "amount": "30ml"
        },
        {
          "name": "피치 리큐르",
          "amount": "20ml"
        },
        {
          "name": "탄산수",
          "amount": "100ml"
        }
      ],
      "recipe": [
        "글라스에 얼음을 채운다",
        "보드카와 피치 리큐르를 넣고 가볍게 섞는다",
        "탄산수를 천천히 따라 넣는다"
      ],
      "reason": "달달한 과일향과 탄산의 톡 쏘는 느낌이 어울리는 칵테일입니다.",
      "tags": ["달콤한", "과일향", "탄산", "부드러운"]
    }
  ],
  "analyzedKeywords": {
    "taste": ["달달한"],
    "texture": ["부드러운"],
    "carbonation": ["톡 쏘는"],
    "flavor": ["과일맛"]
  }
}
```

**Response 필드**

| 필드                                     | 타입     | 설명               |
|----------------------------------------|--------|------------------|
| recommendations                        | Array  | 추천 칵테일 목록 (1~3개) |
| recommendations[].id                   | String | 칵테일 고유 ID        |
| recommendations[].name                 | String | 칵테일 이름           |
| recommendations[].ingredients          | Array  | 재료 목록            |
| recommendations[].ingredients[].name   | String | 재료 이름            |
| recommendations[].ingredients[].amount | String | 재료 용량            |
| recommendations[].recipe               | Array  | 제조 방법 (단계별)      |
| recommendations[].reason               | String | 추천 이유            |
| recommendations[].tags                 | Array  | 칵테일 특징 태그        |
| analyzedKeywords                       | Object | 분석된 키워드 (카테고리별)  |

**Response (Error - 400)**
```json
{
  "error": "INVALID_REQUEST",
  "message": "query는 필수 항목입니다.",
  "timestamp": "2026-03-18T14:30:00Z"
}
```

**Response (Error - 500)**
```json
{
  "error": "INTERNAL_ERROR",
  "message": "서버 내부 오류가 발생했습니다.",
  "timestamp": "2026-03-18T14:30:00Z"
}
```

### 2.2 헬스체크

**Endpoint**
```
GET /api/v1/health
```

**Response (200)**
```json
{
  "status": "UP",
  "timestamp": "2026-03-18T14:30:00Z"
}
```

## 3. 데이터 모델 명세

**구현 형식**
- 웹 계층 DTO: Java record로 구현 (불변 객체)
- 도메인 모델: 일반 클래스 (Lombok 사용)

### 3.1 CocktailRecommendationRequest (record)
사용자의 칵테일 추천 요청 - 웹 계층 DTO

| 필드    | 타입     | 필수 | 설명          |
|-------|--------|----|-----------  |
| query | String | O  | 자연어 입력 문장  |

### 3.2 CocktailRecommendationResponse (record)
칵테일 추천 응답 - 웹 계층 DTO

| 필드                | 타입                  | 필수 | 설명          |
|-------------------|---------------------|----|-------------|
| recommendations   | Array[CocktailDto]  | O  | 추천 칵테일 목록  |
| analyzedKeywords  | AnalyzedKeywords    | O  | 분석된 키워드    |

### 3.3 CocktailDto (record)
칵테일 정보 - 웹 계층 DTO

| 필드          | 타입                | 필수 | 설명           |
|-------------|-------------------|----|--------------|
| id          | String            | O  | 칵테일 고유 ID   |
| name        | String            | O  | 칵테일 이름      |
| ingredients | Array[Ingredient] | O  | 재료 목록       |
| recipe      | Array[String]     | O  | 제조 방법 단계   |
| reason      | String            | O  | 추천 이유       |
| tags        | Array[String]     | O  | 특징 태그       |

### 3.4 Ingredient (record)
재료 정보 - 도메인 모델

| 필드     | 타입     | 필수 | 설명    |
|--------|--------|----|----- |
| name   | String | O  | 재료 이름 |
| amount | String | O  | 재료 용량 |

### 3.5 AnalyzedKeywords (record)
분석된 키워드 정보 - 웹 계층 DTO

| 필드          | 타입            | 필수 | 설명      |
|-------------|---------------|----|---------  |
| taste       | Array[String] | X  | 맛 키워드   |
| texture     | Array[String] | X  | 질감 키워드  |
| carbonation | Array[String] | X  | 자극감 키워드 |
| flavor      | Array[String] | X  | 향 키워드   |
| mood        | Array[String] | X  | 분위기 키워드 |

### 3.6 ErrorResponse (record)
에러 응답 - 웹 계층 DTO

| 필드        | 타입     | 필수 | 설명         |
|-----------|--------|----|-----------  |
| error     | String | O  | 에러 코드     |
| message   | String | O  | 에러 메시지   |
| timestamp | String | O  | 에러 발생 시각 |

## 4. 칵테일 추천 모델 명세

### 4.1 모델 개요
사용자 입력에서 키워드를 분석하고 적합한 칵테일을 추천하는 자체 개발 알고리즘

### 4.2 모델 입력
```
{
  "userQuery": "달달하고 톡 쏘는 과일맛이 나는 부드러운 칵테일 추천해줘"
}
```

**입력 필드**

| 필드        | 타입     | 설명          |
|-----------|--------|-------------|
| userQuery | String | 사용자 입력 문장  |

### 4.3 모델 출력
```
{
  "analyzedKeywords": {
    "taste": ["달달한"],
    "texture": ["부드러운"],
    "carbonation": ["톡 쏘는"],
    "flavor": ["과일맛"],
    "mood": []
  },
  "recommendations": [
    {
      "cocktailId": "cocktail-001",
      "cocktailName": "피치 스파클",
      "matchScore": 0.92
    }
  ]
}
```

**출력 필드**

| 필드                                | 타입     | 설명             |
|-----------------------------------|--------|----------------|
| analyzedKeywords                  | Object | 분석된 키워드 정보    |
| analyzedKeywords.taste            | Array  | 맛 키워드 목록      |
| analyzedKeywords.texture          | Array  | 질감 키워드 목록     |
| analyzedKeywords.carbonation      | Array  | 자극감 키워드 목록    |
| analyzedKeywords.flavor           | Array  | 향 키워드 목록      |
| analyzedKeywords.mood             | Array  | 분위기 키워드 목록    |
| recommendations                   | Array  | 추천 칵테일 목록     |
| recommendations[].cocktailId      | String | 칵테일 ID         |
| recommendations[].cocktailName    | String | 칵테일 이름        |
| recommendations[].matchScore      | Double | 매칭 점수 (0.0~1.0) |

### 4.4 지원 키워드

**맛 (taste)**
- 달달한, 달콤한, 단
- 쓴, 씁쓸한
- 새콤한, 신, 시큼한
- 상큼한

**질감 (texture)**
- 부드러운
- 묵직한, 진한
- 깔끔한, 산뜻한

**자극감 (carbonation)**
- 톡 쏘는, 탄산
- 강한, 독한
- 순한, 약한

**향 (flavor)**
- 과일향, 과일맛
- 시트러스, 레몬, 오렌지
- 허브, 민트
- 베리

**분위기 (mood)**
- 여름에 어울리는
- 가볍게 마시기 좋은
- 파티용
- 로맨틱한

## 5. 비즈니스 로직

### 5.1 추천 프로세스

```
1. 사용자 입력 검증
   - query 필드 존재 여부 확인
   - 빈 문자열 체크

2. 키워드 분석
   - 추천 모델에 사용자 쿼리 전달
   - 맛, 질감, 향, 분위기 키워드 추출

3. 칵테일 매칭
   - 분석된 키워드 기반으로 칵테일 검색
   - 매칭 점수 계산
   - 상위 1~3개 칵테일 선정

4. 레시피 정보 조회
   - 선정된 칵테일의 상세 정보 조회
   - 재료, 제조법, 추천 이유 생성

5. 응답 생성
   - 추천 칵테일 목록과 분석된 키워드 반환
```

## 6. 에러 처리

### 6.1 에러 코드

| 코드              | HTTP 상태 | 설명           |
|-----------------|---------|--------------|
| INVALID_REQUEST | 400     | 요청 파라미터 오류  |
| INTERNAL_ERROR  | 500     | 내부 서버 오류    |

### 6.2 에러 응답 형식
```json
{
  "error": "ERROR_CODE",
  "message": "사용자에게 표시할 에러 메시지",
  "timestamp": "ISO 8601 형식의 타임스탬프"
}
```

## 7. 테스트 케이스

### 7.1 정상 케이스
- 맛 키워드만 포함된 입력
- 질감 키워드만 포함된 입력
- 복합 키워드 포함된 입력
- 분위기 키워드 포함된 입력

### 7.2 비정상 케이스
- 빈 문자열 입력
- null 입력
- query 필드 누락
