-- Cocktail data initialization

INSERT INTO cocktail (id, name, recipe) VALUES (1, '모히또', '글라스에 민트 잎과 설탕을 넣고 으깬다
라임 주스와 럼을 추가한다
얼음을 채우고 탄산수를 부은 뒤 가볍게 섞는다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (1, '화이트 럼', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (1, '라임 주스', '20ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (1, '민트 잎', '10장');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (1, '설탕', '2티스푼');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (1, '탄산수', '100ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'taste', '상큼한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'carbonation', '톡 쏘는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'flavor', '민트');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'flavor', '시트러스');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'mood', '여름에 어울리는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (1, 'mood', '가볍게 마시기 좋은');

INSERT INTO cocktail (id, name, recipe) VALUES (2, '마가리타', '글라스 입구에 소금을 묻힌다
셰이커에 재료와 얼음을 넣고 흔든다
준비한 글라스에 따른다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (2, '데킬라', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (2, '트리플섹', '25ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (2, '라임 주스', '25ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (2, '소금', '적당량');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (2, 'taste', '새콤한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (2, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (2, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (2, 'flavor', '시트러스');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (2, 'mood', '파티용');

INSERT INTO cocktail (id, name, recipe) VALUES (3, '피나콜라다', '블렌더에 모든 재료와 얼음을 넣는다
부드럽게 갈아준다
글라스에 따르고 파인애플로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (3, '화이트 럼', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (3, '파인애플 주스', '80ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (3, '코코넛 크림', '30ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'texture', '부드러운');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'texture', '묵직한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'flavor', '과일향');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'flavor', '코코넛');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (3, 'mood', '여름에 어울리는');

INSERT INTO cocktail (id, name, recipe) VALUES (4, '코스모폴리탄', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔든다
마티니 글라스에 따른다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (4, '보드카', '40ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (4, '트리플섹', '15ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (4, '크랜베리 주스', '30ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (4, '라임 주스', '15ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'taste', '새콤한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'flavor', '베리');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'mood', '로맨틱한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (4, 'mood', '파티용');

INSERT INTO cocktail (id, name, recipe) VALUES (5, '위스키 사워', '셰이커에 재료를 넣고 먼저 드라이 셰이크한다
얼음을 넣고 다시 흔든다
글라스에 따르고 비터스를 떨어뜨린다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (5, '버번 위스키', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (5, '레몬 주스', '25ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (5, '설탕 시럽', '15ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (5, '달걀 흰자', '1개 분량');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (5, 'taste', '새콤한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (5, 'texture', '부드러운');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (5, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (5, 'flavor', '시트러스');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (5, 'mood', '가볍게 마시기 좋은');

INSERT INTO cocktail (id, name, recipe) VALUES (6, '올드 패션드', '글라스에 설탕과 비터스를 넣고 으깬다
위스키를 넣고 얼음을 추가한다
오렌지 필로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (6, '버번 위스키', '60ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (6, '앙고스투라 비터스', '2대시');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (6, '설탕', '1개');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (6, '오렌지 필', '1조각');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (6, 'taste', '쓴');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (6, 'texture', '묵직한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (6, 'carbonation', '강한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (6, 'flavor', '오렌지');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (6, 'mood', '클래식한');

INSERT INTO cocktail (id, name, recipe) VALUES (7, '아페롤 스프리츠', '와인 글라스에 얼음을 채운다
아페롤, 프로세코, 탄산수 순으로 붓는다
가볍게 저어주고 오렌지로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (7, '아페롤', '60ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (7, '프로세코', '90ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (7, '탄산수', '30ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (7, '오렌지 슬라이스', '1조각');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'taste', '쓴');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'taste', '상큼한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'carbonation', '톡 쏘는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'flavor', '오렌지');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'mood', '여름에 어울리는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (7, 'mood', '가볍게 마시기 좋은');

INSERT INTO cocktail (id, name, recipe) VALUES (8, '에스프레소 마티니', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔들어 거품을 낸다
마티니 글라스에 따른다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (8, '보드카', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (8, '커피 리큐르', '25ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (8, '에스프레소', '30ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (8, '설탕 시럽', '10ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'taste', '쓴');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'texture', '부드러운');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'flavor', '커피');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (8, 'mood', '파티용');

INSERT INTO cocktail (id, name, recipe) VALUES (9, '진 토닉', '하이볼 글라스에 얼음을 채운다
진을 붓고 토닉 워터로 채운다
라임을 짜서 넣고 가볍게 저어준다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (9, '진', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (9, '토닉 워터', '150ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (9, '라임', '1조각');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (9, 'taste', '쓴');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (9, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (9, 'carbonation', '톡 쏘는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (9, 'flavor', '허브');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (9, 'mood', '가볍게 마시기 좋은');

INSERT INTO cocktail (id, name, recipe) VALUES (10, '다이키리', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔든다
차가운 칵테일 글라스에 따른다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (10, '화이트 럼', '60ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (10, '라임 주스', '20ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (10, '설탕 시럽', '15ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'taste', '새콤한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'flavor', '시트러스');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (10, 'mood', '가볍게 마시기 좋은');

INSERT INTO cocktail (id, name, recipe) VALUES (11, '모스코 뮬', '구리 머그에 얼음을 채운다
보드카와 라임 주스를 넣는다
진저비어로 채우고 가볍게 저어준다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (11, '보드카', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (11, '라임 주스', '15ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (11, '진저비어', '120ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'taste', '상큼한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'carbonation', '톡 쏘는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'flavor', '생강');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'flavor', '시트러스');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (11, 'mood', '여름에 어울리는');

INSERT INTO cocktail (id, name, recipe) VALUES (12, '네그로니', '올드 패션드 글라스에 얼음을 넣는다
모든 재료를 붓는다
저어주고 오렌지 필로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (12, '진', '30ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (12, '캄파리', '30ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (12, '스위트 베르무트', '30ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (12, 'taste', '쓴');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (12, 'texture', '묵직한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (12, 'carbonation', '강한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (12, 'flavor', '허브');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (12, 'mood', '클래식한');

INSERT INTO cocktail (id, name, recipe) VALUES (13, '블루 하와이', '셰이커에 모든 재료와 얼음을 넣는다
흔들어 섞는다
허리케인 글라스에 따르고 파인애플로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (13, '화이트 럼', '40ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (13, '블루 큐라소', '20ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (13, '파인애플 주스', '60ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (13, '코코넛 크림', '20ml');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'texture', '부드러운');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'flavor', '과일향');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'flavor', '코코넛');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'mood', '여름에 어울리는');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (13, 'mood', '파티용');

INSERT INTO cocktail (id, name, recipe) VALUES (14, '상그리아', '큰 피처에 와인을 붓는다
브랜디, 오렌지 주스, 설탕을 넣는다
과일을 썰어 넣고 최소 2시간 냉장 보관한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (14, '레드 와인', '750ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (14, '브랜디', '50ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (14, '오렌지 주스', '100ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (14, '각종 과일', '적당량');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (14, '설탕', '2큰술');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'taste', '상큼한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'texture', '부드러운');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'flavor', '과일향');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'mood', '파티용');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (14, 'mood', '여름에 어울리는');

INSERT INTO cocktail (id, name, recipe) VALUES (15, '민트 줄렙', '글라스에 민트 잎과 설탕, 물을 넣고 으깬다
크러시 아이스를 가득 채운다
위스키를 붓고 민트로 장식한다');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (15, '버번 위스키', '60ml');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (15, '민트 잎', '8장');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (15, '설탕', '1티스푼');
INSERT INTO cocktail_ingredients (cocktail_id, name, amount) VALUES (15, '물', '약간');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (15, 'taste', '달달한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (15, 'texture', '깔끔한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (15, 'carbonation', '순한');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (15, 'flavor', '민트');
INSERT INTO cocktail_tags (cocktail_id, category, tag) VALUES (15, 'mood', '여름에 어울리는');
