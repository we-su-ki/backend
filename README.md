# Cocktail BE API Guide

## API 목록

| Method | Path                      | 설명               |
|--------|---------------------------|------------------|
| `GET`  | `/cocktails`              | 전체 칵테일 목록 조회     |
| `POST` | `/cocktails/recommend`    | 자연어로 칵테일 추천      |
| `GET`  | `/ingredients`            | 전체 재료 목록 조회      |
| `POST` | `/ingredients/predict`    | 재료 조합으로 맛 프로필 예측 |

### 에러 응답 예시

```json
{
  "error": "Bad Request",
  "message": "Required parameter 'sweetness' is missing",
  "timestamp": "2026-04-17T16:40:12.345"
}
```

---

## 1. 전체 칵테일 목록 조회

> **GET /cocktails**

전체 칵테일 데이터를 반환합니다.

### 요청 예시

```bash
curl -s http://localhost:8080/cocktails
```

### 응답 예시

```json
[
  {
    "name": "Abbey",
    "imageUrl": "https://cdn.diffordsguide.com/cocktail/NAmyA8/lifestyle/1/1024x.webp?v=1737701571",
    "glassRaw": "Serve in a Coupe glass",
    "garnishRaw": "EXPRESS lemon zest twist over the cocktail and use as garnish.",
    "methodRaw": "SHAKE all ingredients with ice. FINE STRAIN into chilled glass.",
    "methodCategory": "Shake",
    "ingredients": [
      { "name": "Hayman's London Dry Gin", "ml": 45 },
      { "name": "Americano bianco", "ml": 22.5 },
      { "name": "Orange juice (freshly squeezed)", "ml": 22.5 },
      { "name": "Angostura Aromatic Bitters", "ml": 0.8 }
    ],
    "scoreStrength": 7,
    "scoreSweetSour": 7,
    "reviewText": "A dry, orangey, herbal, gin-laced aperitivo, closely related to the better known Bronx .",
    "sourceUrl": "https://www.diffordsguide.com/cocktails/recipe/3/abbey",
    "tasteProfile": {
      "abv": 0.0,
      "sweetness": 0.0,
      "sourness": 0.0,
      "bitterness": 0.0,
      "umamiSalty": 0.0,
      "fruity": 0.0,
      "citrus": 0.0,
      "floral": 0.0,
      "herbal": 0.0,
      "spicy": 0.0,
      "woodySmoky": 0.0,
      "body": 0.0,
      "fizzy": 0.0
    },
    "matchScore": 0.0
  }
]
```

### 응답 필드

| 필드                   | 타입       | 설명                   | 비고                |
|----------------------|----------|----------------------|-------------------|
| `name`               | `string` | 칵테일 이름 (PK)          |                   |
| `imageUrl`           | `string` | 이미지 URL              |                   |
| `glassRaw`           | `string` | 잔 종류                 |                   |
| `garnishRaw`         | `string` | 가니시                  |                   |
| `methodRaw`          | `string` | 제조 방법                | 줄바꿈 포함 가능         |
| `methodCategory`     | `string` | 제조 방법 카테고리           | Shake, Build 등    |
| `ingredients`        | `array`  | 재료 목록                |                   |
| `ingredients[].name` | `string` | 재료 이름                |                   |
| `ingredients[].ml`   | `number` | 재료 용량 (ml)           |                   |
| `scoreStrength`      | `number` | 도수 점수 (1~10)         | null 가능           |
| `scoreSweetSour`     | `number` | 단맛/신맛 점수 (1~10)      | null 가능           |
| `reviewText`         | `string` | 리뷰 텍스트               | null 가능           |
| `sourceUrl`          | `string` | 출처 URL               | null 가능           |
| `tasteProfile`       | `object` | 맛 프로필 (13개 축, 아래 참고) |                   |
| `matchScore`         | `number` | 추천 유사도 점수            | 목록 조회에서는 항상 `0.0` |

### tasteProfile 필드

| 필드            | 타입       | 설명      |
|---------------|----------|---------|
| `abv`         | `number` | 도수감     |
| `sweetness`   | `number` | 단맛      |
| `sourness`    | `number` | 산미      |
| `bitterness`  | `number` | 쓴맛      |
| `umamiSalty`  | `number` | 감칠맛/짠맛  |
| `fruity`      | `number` | 과일향     |
| `citrus`      | `number` | 시트러스    |
| `floral`      | `number` | 꽃향      |
| `herbal`      | `number` | 허브향     |
| `spicy`       | `number` | 스파이시    |
| `woodySmoky`  | `number` | 우디/스모키  |
| `body`        | `number` | 바디감     |
| `fizzy`       | `number` | 탄산감     |

### 프론트 참고

- 현재 정렬은 별도로 보장하지 않습니다.

---

## 2. 자연어로 칵테일 추천

> **POST /cocktails/recommend**
>
> Content-Type: application/json

자연어로 원하는 맛을 표현하면, LLM이 맛 벡터로 변환한 뒤 가까운 칵테일 최대 5개를 반환합니다.

### 요청 바디

```json
{ "query": "여름처럼 상큼하고 시원한 칵테일" }
```

| 필드      | 타입       | 설명          |
|---------|----------|-------------|
| `query` | `string` | 자연어 맛 표현 문장 |

### 요청 예시

```bash
curl -s -X POST http://localhost:8080/cocktails/recommend \
  -H "Content-Type: application/json" \
  -d '{"query": "여름처럼 상큼하고 시원한 칵테일"}'
```
한 줄
```bash
curl -s -X POST http://localhost:8080/cocktails/recommend -H "Content-Type: application/json" -d '{"query": "여름처럼 상큼하고 시원한 칵테일"}'
```

### 응답 예시

```json
[
  {
    "name": "Abbey",
    "imageUrl": "https://cdn.diffordsguide.com/cocktail/NAmyA8/lifestyle/1/1024x.webp?v=1737701571",
    "glassRaw": "Serve in a Coupe glass",
    "garnishRaw": "EXPRESS lemon zest twist over the cocktail and use as garnish.",
    "methodRaw": "SHAKE all ingredients with ice. FINE STRAIN into chilled glass.",
    "methodCategory": "Shake",
    "ingredients": [
      { "name": "Hayman's London Dry Gin", "ml": 45 },
      { "name": "Americano bianco", "ml": 22.5 },
      { "name": "Orange juice (freshly squeezed)", "ml": 22.5 },
      { "name": "Angostura Aromatic Bitters", "ml": 0.8 }
    ],
    "scoreStrength": 7,
    "scoreSweetSour": 7,
    "reviewText": "A dry, orangey, herbal, gin-laced aperitivo, closely related to the better known Bronx .",
    "sourceUrl": "https://www.diffordsguide.com/cocktails/recipe/3/abbey",
    "tasteProfile": {
      "abv": 0.0,
      "sweetness": 0.0,
      "sourness": 0.0,
      "bitterness": 0.0,
      "umamiSalty": 0.0,
      "fruity": 0.0,
      "citrus": 0.0,
      "floral": 0.0,
      "herbal": 0.0,
      "spicy": 0.0,
      "woodySmoky": 0.0,
      "body": 0.0,
      "fizzy": 0.0
    },
    "matchScore": 0.92
  }
]
```

### 응답 필드

[1. 전체 칵테일 목록 조회](#1-전체-칵테일-목록-조회)의 응답 필드와 동일

### 참고

- `matchScore`는 `0.0 ~ 1.0` 범위의 유사도이며, 높은 순으로 최대 5개 반환합니다.

---

## 3. 전체 재료 목록 조회

> **GET /ingredients**

전체 재료 목록을 반환합니다.

### 요청 예시

```bash
curl -s http://localhost:8080/ingredients
```

### 응답 예시

```json
[
  { "id": 1, "name": "Amaretto" },
  { "id": 2, "name": "Angostura Bitters" },
  { "id": 3, "name": "Apricot Brandy" }
]
```

### 참고

- `POST /ingredients/predict`에 필요한 `id`를 이 응답으로 바로 사용할 수 있습니다.

---

## 4. 재료 조합으로 맛 프로필 예측

> **POST /ingredients/predict**
> 
> Content-Type: application/json

재료 목록을 보내면 맛 프로필을 반환합니다.

### 요청 바디

```json
{
  "ingredients": [
    { "id": 1, "amount": 50 },
    { "id": 2, "amount": 20 }
  ],
  "methodCategory": "Shake"
}
```

| 필드                     | 타입       | 설명                                                         | 비고                          |
|------------------------|----------|------------------------------------------------------------|-----------------------------|
| `ingredients`          | `array`  | 재료 목록                                                      |                             |
| `ingredients[].id`     | `number` | 재료 ID                                                      |                             |
| `ingredients[].amount` | `number` | 재료 양(ml)                                                   |                             |
| `methodCategory`       | `string` | 제조 방법 카테고리(`Blend`, `Build`, `Float`, `Shake`, `Stir` 중 1) | null 또는 알 수 없는 값은 `NONE` 처리 |

### 요청 예시

```bash
curl -s -X POST http://localhost:8080/ingredients/predict \
  -H "Content-Type: application/json" \
  -d '{
    "ingredients": [
      { "id": 1, "amount": 50 },
      { "id": 2, "amount": 20 }
    ],
    "methodCategory": "Shake"
  }'
```
한 줄
```bash
curl -s -X POST http://localhost:8080/ingredients/predict -H "Content-Type: application/json" -d '{"ingredients":[{"id":1,"amount":50},{"id":2,"amount":20}],"methodCategory":"Shake"}'
```

### 응답 예시

```json
{
  "abv": 0.0,
  "sweetness": 5.17,
  "sourness": 4.61,
  "bitterness": 5.57,
  "umamiSalty": 1.55,
  "fruity": 4.49,
  "citrus": 4.83,
  "floral": 1.14,
  "herbal": 3.28,
  "spicy": 2.42,
  "woodySmoky": 1.83,
  "body": 3.71,
  "fizzy": 4.24
}
```
