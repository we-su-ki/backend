# API 명세

## 개요

칵테일 레시피 추천 서비스의 REST API 명세입니다.

- **Base URL**: `http://localhost:8080`
- **API Version**: v1
- **Content-Type**: `application/json`
- **Character Encoding**: UTF-8

---

## 엔드포인트 목록

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/v1/cocktails/analyze` | 키워드 분석 |
| GET | `/api/v1/cocktails/recommend` | 칵테일 추천 |

---

## 1. 키워드 분석

사용자 입력에서 감각 디스크립터를 추출합니다.

### Request

```
GET /api/v1/cocktails/analyze
```

#### Query Parameters

| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| query | String | O | 분석할 자연어 텍스트 (공백 불가) |

#### 예시

```bash
curl "http://localhost:8080/api/v1/cocktails/analyze?query=달달한%20칵테일"
```

### Response

#### 200 OK

```json
{
  "taste": ["sweet"],
  "aroma": [],
  "mouthfeel": [],
  "sensation": [],
  "impression": []
}
```

#### Response Body

| 필드 | 타입 | 설명 |
|-----|------|------|
| taste | Array<String> | 맛 디스크립터 (예: sweet, sour, bitter) |
| aroma | Array<String> | 향 디스크립터 (예: fruity, floral, citrus) |
| mouthfeel | Array<String> | 질감 디스크립터 (예: smooth, creamy, light) |
| sensation | Array<String> | 감각 디스크립터 (예: refreshing, carbonated) |
| impression | Array<String> | 인상 디스크립터 (예: elegant, tropical) |

#### 400 Bad Request

쿼리 파라미터가 누락되거나 빈 문자열인 경우

```json
{
  "error": "Bad Request",
  "message": "query must not be blank",
  "timestamp": "2026-03-19T10:30:00"
}
```

---

## 2. 칵테일 추천

사용자 쿼리에 기반하여 칵테일을 추천합니다.

### Request

```
GET /api/v1/cocktails/recommend
```

#### Query Parameters

| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| query | String | O | 추천받을 칵테일의 특징 (공백 불가) |

#### 예시

```bash
curl "http://localhost:8080/api/v1/cocktails/recommend?query=달달하고%20과일향%20나는%20칵테일"
```

### Response

#### 200 OK

```json
{
  "cocktails": [
    {
      "id": 3,
      "name": "피나콜라다",
      "ingredients": [
        {
          "name": "화이트 럼",
          "amount": "50ml"
        },
        {
          "name": "파인애플 주스",
          "amount": "80ml"
        },
        {
          "name": "코코넛 크림",
          "amount": "30ml"
        }
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

#### Response Body

| 필드 | 타입 | 설명 |
|-----|------|------|
| cocktails | Array<Cocktail> | 추천 칵테일 목록 (최대 3개, 점수 내림차순) |
| count | Integer | 추천된 칵테일 개수 |

##### Cocktail Object

| 필드 | 타입 | 설명 |
|-----|------|------|
| id | Long | 칵테일 ID |
| name | String | 칵테일 이름 |
| ingredients | Array<Ingredient> | 재료 목록 |
| recipe | String | 레시피 (줄바꿈으로 단계 구분) |
| score | Double | 매칭 점수 (0.0 ~ 1.0) |
| reason | String | 추천 이유 |
| matchedKeywords | Array<String> | 매칭된 키워드 목록 |

##### Ingredient Object

| 필드 | 타입 | 설명 |
|-----|------|------|
| name | String | 재료 이름 |
| amount | String | 재료 양 |

#### 매칭 없음 (200 OK)

쿼리와 매칭되는 칵테일이 없는 경우

```json
{
  "cocktails": [],
  "count": 0
}
```

#### 400 Bad Request

쿼리 파라미터가 누락되거나 빈 문자열인 경우

```json
{
  "error": "Bad Request",
  "message": "query must not be blank",
  "timestamp": "2026-03-19T10:30:00"
}
```

---

## 매칭 알고리즘

### Recall 기반 점수 계산

```
score = |쿼리 키워드 ∩ 칵테일 키워드| / |쿼리 키워드|
```

- 사용자가 입력한 키워드 중 몇 개가 매칭되었는지 비율로 계산
- 예: 쿼리 키워드 3개 중 3개 매칭 → score = 1.0
- 예: 쿼리 키워드 3개 중 2개 매칭 → score = 0.67

### 추천 규칙

1. **최대 개수**: 최대 3개 칵테일 반환
2. **정렬**: 점수 내림차순 정렬
3. **동점**: 동점인 경우 ID 오름차순
4. **최소 점수**: 0.0 초과 (매칭되지 않은 칵테일은 제외)

---

## 에러 응답

### ErrorResponse

| 필드 | 타입 | 설명 |
|-----|------|------|
| error | String | HTTP 상태 메시지 |
| message | String | 에러 상세 메시지 |
| timestamp | String | 에러 발생 시각 (ISO 8601) |

### 에러 코드

| 상태 코드 | 설명 | 예시 |
|----------|------|------|
| 400 | 잘못된 요청 (필수 파라미터 누락, 빈 문자열 등) | `query must not be blank` |
| 500 | 서버 내부 오류 | `Internal server error` |

---

## 사용 예시

### 1. 단일 키워드 추천

```bash
curl "http://localhost:8080/api/v1/cocktails/recommend?query=달달한"
```

**응답**: 달달한 맛을 가진 칵테일 최대 3개

### 2. 복합 키워드 추천

```bash
curl "http://localhost:8080/api/v1/cocktails/recommend?query=달달하고%20상큼한%20톡%20쏘는%20칵테일"
```

**응답**: 3가지 특성을 모두 만족하거나 부분 만족하는 칵테일

### 3. 키워드 분석 후 추천

```bash
# 1단계: 키워드 분석
curl "http://localhost:8080/api/v1/cocktails/analyze?query=파티용%20화려한%20칵테일"

# 2단계: 분석된 키워드로 추천
curl "http://localhost:8080/api/v1/cocktails/recommend?query=파티용%20화려한%20칵테일"
```

### 4. 매칭 없음 케이스

```bash
curl "http://localhost:8080/api/v1/cocktails/recommend?query=완전히존재하지않는맛"
```

**응답**:
```json
{
  "cocktails": [],
  "count": 0
}
```

### 5. 에러 케이스

```bash
# 파라미터 누락
curl "http://localhost:8080/api/v1/cocktails/recommend"

# 빈 문자열
curl "http://localhost:8080/api/v1/cocktails/recommend?query="

# 공백만
curl "http://localhost:8080/api/v1/cocktails/recommend?query=%20%20%20"
```

모두 400 Bad Request 반환

---

## 성능

- **서버 시작 시간**: ~2초
- **평균 응답 시간**: 6ms
- **첫 요청 응답 시간**: 47ms (워밍업)

---

## 제약사항

1. **쿼리 길이**: 제한 없음 (단, 공백만으로 구성 불가)
2. **동시 요청**: 제한 없음 (stateless 설계)
3. **칵테일 DB**: 현재 H2 in-memory (서버 재시작 시 초기화)
4. **캐싱**: 미적용 (매 요청마다 실시간 계산)

---

## 버전 히스토리

| 버전 | 날짜 | 변경사항 |
|-----|------|---------|
| v1 | 2026-03-19 | 초기 API 릴리스 |
