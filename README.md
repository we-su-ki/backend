# Cocktail BE API Guide

## API 목록

| Method | Path                   | 설명                |
|--------|------------------------|-------------------|
| `GET`  | `/cocktails`           | 전체 칵테일 목록 조회      |
| `GET`  | `/cocktails/match`     | 맛 프로필 조건으로 칵테일 추천 |
| `GET`  | `/ingredients`         | 전체 재료 목록 조회       |
| `POST` | `/ingredients/predict` | 재료 조합으로 맛 프로필 예측  |

### 에러 응답

```json
{
  "error": "Bad Request",
  "message": "Required parameter 'sweet' is missing",
  "timestamp": "2026-04-17T16:40:12.345"
}
```

---

## 1. 전체 칵테일 목록 조회

> **GET /cocktails**

전체 칵테일 데이터를 반환합니다.

### 요청 예시

```bash
curl -s http://localhost:8080/cocktails | jq
```

### 응답 예시

```json
[
  {
    "id": 1,
    "name": "모히또",
    "imageUrl": "https://.../mojito.jpg",
    "ingredients": [
      { "name": "화이트 럼", "amount": 50 },
      { "name": "라임 주스", "amount": 20 },
      { "name": "민트 잎", "amount": 0 },
      { "name": "설탕", "amount": 0 },
      { "name": "탄산수", "amount": 100 }
    ],
    "tasteProfile": {
      "sweet": 0.3,
      "body": 0.2,
      "bitter": 0.1,
      "abv": 0.5,
      "smoky": 0.0,
      "sour": 0.6
    },
    "recipe": "글라스에 민트 잎과 설탕을 넣고 으깬다\n라임 주스와 럼을 추가한다\n얼음을 채우고 탄산수를 부은 뒤 가볍게 섞는다",
    "score": 0.0
  }
]
```

### 응답 필드

| 필드                     | 타입       | 설명                |
|------------------------|----------|-------------------|
| `id`                   | `number` | 칵테일 ID            |
| `name`                 | `string` | 칵테일 이름            |
| `imageUrl`             | `string` | 이미지 URL           |
| `ingredients`          | `array`  | 재료 목록             |
| `ingredients[].name`   | `string` | 재료 이름             |
| `ingredients[].amount` | `number` | 재료 양              |
| `tasteProfile`         | `object` | 맛 프로필             |
| `recipe`               | `string` | 제조 방법. 줄바꿈 포함 가능  |
| `score`                | `number` | 목록 조회에서는 항상 `0.0` |

### 프론트 참고

- 현재 정렬은 별도로 보장하지 않습니다.
- `recipe`는 줄바꿈 문자열을 포함합니다.

---

## 2. 맛 기준 칵테일 추천

> **GET /cocktails/match**

맛 축 6개 중 원하는 값만 골라 전달하면, 해당 조건과 가까운 칵테일 최대 5개를 반환합니다.

### 사용 가능한 쿼리 파라미터

| 이름       | 타입       | 설명   |
|----------|----------|------|
| `sweet`  | `number` | 단맛   |
| `body`   | `number` | 바디감  |
| `bitter` | `number` | 쓴맛   |
| `abv`    | `number` | 도수감  |
| `smoky`  | `number` | 스모키함 |
| `sour`   | `number` | 산미   |

### 요청 예시 1

```bash
curl -s "http://localhost:8080/cocktails/match?sweet=4.0" | jq
```

### 요청 예시 2

```bash
curl -s "http://localhost:8080/cocktails/match?sweet=4.0&sour=2.0&abv=3.0" | jq
```

추천 결과에서 이름과 점수만 보기

```bash
curl -s "http://localhost:8080/cocktails/match?sweet=4.0&sour=2.0" \
  | jq '.[] | {name, score}'
```

### 응답 예시

```json
[
  {
    "id": 3,
    "name": "피나콜라다",
    "imageUrl": "https://.../pina-colada.jpg",
    "ingredients": [
      { "name": "화이트 럼", "amount": 50 },
      { "name": "파인애플 주스", "amount": 80 },
      { "name": "코코넛 크림", "amount": 30 }
    ],
    "tasteProfile": {
      "sweet": 0.7,
      "body": 0.7,
      "bitter": 0.0,
      "abv": 0.5,
      "smoky": 0.0,
      "sour": 0.0
    },
    "recipe": "블렌더에 모든 재료와 얼음을 넣는다\n부드럽게 갈아준다\n글라스에 따르고 파인애플로 장식한다",
    "score": 0.92
  }
]
```

### 응답 필드

| 필드                     | 타입       | 설명               |
|------------------------|----------|------------------|
| `id`                   | `number` | 칵테일 ID           |
| `name`                 | `string` | 칵테일 이름           |
| `imageUrl`             | `string` | 이미지 URL          |
| `ingredients`          | `array`  | 재료 목록            |
| `ingredients[].name`   | `string` | 재료 이름            |
| `ingredients[].amount` | `number` | 재료 양             |
| `tasteProfile`         | `object` | 맛 프로필            |
| `recipe`               | `string` | 제조 방법. 줄바꿈 포함 가능 |
| `score`                | `number` | 추천 점수            |

### 참고

- 전달한 파라미터만 비교합니다. 6개 축 중 일부만 보내도 됩니다.
- 점수는 `0.0 ~ 1.0` 범위의 유사도입니다.
- 점수가 높은 순으로 최대 5개 반환합니다.
- 쿼리 파라미터를 하나도 보내지 않으면 빈 배열을 반환합니다.

---

## 3. 전체 재료 목록 조회

> **GET /ingredients**

전체 재료 목록을 반환합니다.

### 요청 예시

```bash
curl -s http://localhost:8080/ingredients | jq
```

### 응답 예시

```json
[
  { "id": 1, "name": "화이트 럼" },
  { "id": 2, "name": "라임 주스" },
  { "id": 3, "name": "민트 잎" }
]
```

### 참고

- `POST /ingredients/predict`에 필요한 `id`를 이 응답으로 바로 사용할 수 있습니다.

---

## 4. 재료 조합으로 맛 프로필 예측

> **POST /ingredients/predict**

재료 목록을 보내면 맛 프로필을 반환합니다.

### 요청 바디

```json
{
  "ingredients": [
    { "id": 1, "amount": 50 },
    { "id": 2, "amount": 20 }
  ]
}
```

| 필드                     | 타입       | 설명    |
|------------------------|----------|-------|
| `ingredients`          | `array`  | 재료 목록 |
| `ingredients[].id`     | `number` | 재료 ID |
| `ingredients[].amount` | `number` | 재료 양  |

### 요청 예시

```bash
curl -s -X POST http://localhost:8080/ingredients/predict \
  -H "Content-Type: application/json" \
  -d '{
    "ingredients": [
      { "id": 1, "amount": 50 },
      { "id": 2, "amount": 20 }
    ]
  }' | jq
```

### 응답 예시

```json
{
  "sweet": 0.1,
  "body": 0.2,
  "bitter": 0.3,
  "abv": 0.0,
  "smoky": 0.0,
  "sour": 0.5
}
```
