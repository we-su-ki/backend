-- Cocktail data initialization
-- Flavor vector column order:
-- abv, carbonated, sweetness, sourness, bitterness, body,
-- apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest,
-- earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee,
-- clove, coffee, floral, licorice, malty, mint, nutmeg,
-- peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry,
-- bitter, brine, creamy, ginger, herbal, maple, nutty, oak,
-- salty, smokey, sour, spicy, sweet, caramel

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (1, '모히또', '/images/mojito.jpg', '글라스에 민트 잎과 설탕을 넣고 으깬다
라임 주스와 럼을 추가한다
얼음을 채우고 탄산수를 부은 뒤 가볍게 섞는다',
0.5, 0.8, 0.3, 0.6, 0.1, 0.2, 0.0, 0.0, 0.0, 0.6, 0.0, 0.3, 0.0, 0.0, 0.0, 0.3,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0,
0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.6, 0.0, 0.3, 0.0);

INSERT INTO ingredient (id, name) VALUES (1, '화이트 럼');
INSERT INTO ingredient (id, name) VALUES (2, '라임 주스');
INSERT INTO ingredient (id, name) VALUES (3, '민트 잎');
INSERT INTO ingredient (id, name) VALUES (4, '설탕');
INSERT INTO ingredient (id, name) VALUES (5, '탄산수');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (1, 1, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (1, 2, 20);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (1, 3, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (1, 4, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (1, 5, 100);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (2, '마가리타', '/images/margarita.jpg', '글라스 입구에 소금을 묻힌다
셰이커에 재료와 얼음을 넣고 흔든다
준비한 글라스에 따른다',
0.7, 0.0, 0.2, 0.7, 0.1, 0.3, 0.0, 0.0, 0.0, 0.7, 0.0, 0.5, 0.0, 0.0, 0.0, 0.3,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.6, 0.0, 0.2, 0.0);

INSERT INTO ingredient (id, name) VALUES (6, '데킬라');
INSERT INTO ingredient (id, name) VALUES (7, '트리플섹');
INSERT INTO ingredient (id, name) VALUES (8, '라임 주스');
INSERT INTO ingredient (id, name) VALUES (9, '소금');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (2, 6, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (2, 7, 25);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (2, 8, 25);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (2, 9, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (3, '피나콜라다', '/images/pina-colada.jpg', '블렌더에 모든 재료와 얼음을 넣는다
부드럽게 갈아준다
글라스에 따르고 파인애플로 장식한다',
0.5, 0.0, 0.7, 0.0, 0.0, 0.7, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.0);

INSERT INTO ingredient (id, name) VALUES (10, '화이트 럼');
INSERT INTO ingredient (id, name) VALUES (11, '파인애플 주스');
INSERT INTO ingredient (id, name) VALUES (12, '코코넛 크림');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (3, 10, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (3, 11, 80);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (3, 12, 30);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (4, '코스모폴리탄', '/images/cosmopolitan.jpg', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔든다
마티니 글라스에 따른다',
0.6, 0.0, 0.4, 0.4, 0.1, 0.3, 0.0, 0.0, 0.3, 0.5, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.4, 0.0);

INSERT INTO ingredient (id, name) VALUES (13, '보드카');
INSERT INTO ingredient (id, name) VALUES (14, '트리플섹');
INSERT INTO ingredient (id, name) VALUES (15, '크랜베리 주스');
INSERT INTO ingredient (id, name) VALUES (16, '라임 주스');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (4, 13, 40);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (4, 14, 15);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (4, 15, 30);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (4, 16, 15);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (5, '위스키 사워', '/images/whiskey-sour.jpg', '셰이커에 재료를 넣고 먼저 드라이 셰이크한다
얼음을 넣고 다시 흔든다
글라스에 따르고 비터스를 떨어뜨린다',
0.7, 0.0, 0.4, 0.7, 0.2, 0.4, 0.0, 0.0, 0.0, 0.3, 0.0, 0.7, 0.0, 0.0, 0.0, 0.2,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0, 0.3, 0.3);

INSERT INTO ingredient (id, name) VALUES (17, '버번 위스키');
INSERT INTO ingredient (id, name) VALUES (18, '레몬 주스');
INSERT INTO ingredient (id, name) VALUES (19, '설탕 시럽');
INSERT INTO ingredient (id, name) VALUES (20, '달걀 흰자');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (5, 17, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (5, 18, 25);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (5, 19, 15);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (5, 20, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (6, '올드 패션드', '/images/old-fashioned.jpg', '글라스에 설탕과 비터스를 넣고 으깬다
위스키를 넣고 얼음을 추가한다
오렌지 필로 장식한다',
0.8, 0.0, 0.3, 0.0, 0.4, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.5, 0.4, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.5);

INSERT INTO ingredient (id, name) VALUES (21, '버번 위스키');
INSERT INTO ingredient (id, name) VALUES (22, '앙고스투라 비터스');
INSERT INTO ingredient (id, name) VALUES (23, '설탕');
INSERT INTO ingredient (id, name) VALUES (24, '오렌지 필');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (6, 21, 60);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (6, 22, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (6, 23, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (6, 24, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (7, '아페롤 스프리츠', '/images/aperol-spritz.jpg', '와인 글라스에 얼음을 채운다
아페롤, 프로세코, 탄산수 순으로 붓는다
가볍게 저어주고 오렌지로 장식한다',
0.3, 0.8, 0.3, 0.3, 0.4, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.2,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.3, 0.0);

INSERT INTO ingredient (id, name) VALUES (25, '아페롤');
INSERT INTO ingredient (id, name) VALUES (26, '프로세코');
INSERT INTO ingredient (id, name) VALUES (27, '탄산수');
INSERT INTO ingredient (id, name) VALUES (28, '오렌지 슬라이스');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (7, 25, 60);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (7, 26, 90);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (7, 27, 30);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (7, 28, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (8, '에스프레소 마티니', '/images/espresso-martini.jpg', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔들어 거품을 낸다
마티니 글라스에 따른다',
0.7, 0.0, 0.4, 0.0, 0.3, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.9, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0.2, 0.0, 0.0, 0.6, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.3);

INSERT INTO ingredient (id, name) VALUES (29, '보드카');
INSERT INTO ingredient (id, name) VALUES (30, '커피 리큐르');
INSERT INTO ingredient (id, name) VALUES (31, '에스프레소');
INSERT INTO ingredient (id, name) VALUES (32, '설탕 시럽');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (8, 29, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (8, 30, 25);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (8, 31, 30);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (8, 32, 10);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (9, '진 토닉', '/images/gin-tonic.jpg', '하이볼 글라스에 얼음을 채운다
진을 붓고 토닉 워터로 채운다
라임을 짜서 넣고 가볍게 저어준다',
0.5, 0.7, 0.1, 0.2, 0.4, 0.2, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0.1, 0.0);

INSERT INTO ingredient (id, name) VALUES (33, '진');
INSERT INTO ingredient (id, name) VALUES (34, '토닉 워터');
INSERT INTO ingredient (id, name) VALUES (35, '라임');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (9, 33, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (9, 34, 150);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (9, 35, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (10, '다이키리', '/images/daiquiri.jpg', '셰이커에 모든 재료와 얼음을 넣는다
강하게 흔든다
차가운 칵테일 글라스에 따른다',
0.6, 0.0, 0.4, 0.7, 0.1, 0.2, 0.0, 0.0, 0.0, 0.6, 0.0, 0.5, 0.0, 0.0, 0.0, 0.3,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0, 0.4, 0.0);

INSERT INTO ingredient (id, name) VALUES (36, '화이트 럼');
INSERT INTO ingredient (id, name) VALUES (37, '라임 주스');
INSERT INTO ingredient (id, name) VALUES (38, '설탕 시럽');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (10, 36, 60);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (10, 37, 20);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (10, 38, 15);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (11, '모스코 뮬', '/images/moscow-mule.jpg', '구리 머그에 얼음을 채운다
보드카와 라임 주스를 넣는다
진저비어로 채우고 가볍게 저어준다',
0.5, 0.8, 0.1, 0.3, 0.1, 0.2, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.4, 0.1, 0.0);

INSERT INTO ingredient (id, name) VALUES (39, '보드카');
INSERT INTO ingredient (id, name) VALUES (40, '라임 주스');
INSERT INTO ingredient (id, name) VALUES (41, '진저비어');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (11, 39, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (11, 40, 15);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (11, 41, 120);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (12, '네그로니', '/images/negroni.jpg', '올드 패션드 글라스에 얼음을 넣는다
모든 재료를 붓는다
저어주고 오렌지 필로 장식한다',
0.7, 0.0, 0.1, 0.0, 0.7, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0);

INSERT INTO ingredient (id, name) VALUES (42, '진');
INSERT INTO ingredient (id, name) VALUES (43, '캄파리');
INSERT INTO ingredient (id, name) VALUES (44, '스위트 베르무트');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (12, 42, 30);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (12, 43, 30);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (12, 44, 30);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (13, '블루 하와이', '/images/blue-hawaii.jpg', '셰이커에 모든 재료와 얼음을 넣는다
흔들어 섞는다
허리케인 글라스에 따르고 파인애플로 장식한다',
0.4, 0.0, 0.7, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0, 0.3, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.7, 0.0);

INSERT INTO ingredient (id, name) VALUES (45, '화이트 럼');
INSERT INTO ingredient (id, name) VALUES (46, '블루 큐라소');
INSERT INTO ingredient (id, name) VALUES (47, '파인애플 주스');
INSERT INTO ingredient (id, name) VALUES (48, '코코넛 크림');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (13, 45, 40);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (13, 46, 20);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (13, 47, 60);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (13, 48, 20);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (14, '상그리아', '/images/sangria.jpg', '큰 피처에 와인을 붓는다
브랜디, 오렌지 주스, 설탕을 넣는다
과일을 썰어 넣고 최소 2시간 냉장 보관한다',
0.4, 0.0, 0.6, 0.3, 0.2, 0.5, 0.0, 0.0, 0.5, 0.0, 0.7, 0.0, 0.3, 0.0, 0.2, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.6, 0.2);

INSERT INTO ingredient (id, name) VALUES (49, '레드 와인');
INSERT INTO ingredient (id, name) VALUES (50, '브랜디');
INSERT INTO ingredient (id, name) VALUES (51, '오렌지 주스');
INSERT INTO ingredient (id, name) VALUES (52, '각종 과일');
INSERT INTO ingredient (id, name) VALUES (53, '설탕');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (14, 49, 750);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (14, 50, 50);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (14, 51, 100);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (14, 52, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (14, 53, 0);

INSERT INTO cocktail (id, name, image_url, recipe, abv, carbonated, sweetness, sourness, bitterness, body, apple, banana, cherry, citrus, fruity, lemon, orange, pear, raisins, zest, earthy, barley, buttery, butterscotch, candy, chocolate, cinnamon, cocoa, corn, honey, tea, toffee, clove, coffee, floral, licorice, malty, mint, nutmeg, peaty, peppery, roses, spices, sugar, tobacco, vanilla, wood, sherry, bitter, brine, creamy, ginger, herbal, maple, nutty, oak, salty, smokey, sour, spicy, sweet, caramel)
VALUES (15, '민트 줄렙', '/images/mint-julep.jpg', '글라스에 민트 잎과 설탕, 물을 넣고 으깬다
크러시 아이스를 가득 채운다
위스키를 붓고 민트로 장식한다',
0.7, 0.0, 0.4, 0.0, 0.1, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.9, 0.0,
0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.3);

INSERT INTO ingredient (id, name) VALUES (54, '버번 위스키');
INSERT INTO ingredient (id, name) VALUES (55, '민트 잎');
INSERT INTO ingredient (id, name) VALUES (56, '설탕');
INSERT INTO ingredient (id, name) VALUES (57, '물');
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (15, 54, 60);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (15, 55, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (15, 56, 0);
INSERT INTO cocktail_ingredient (cocktail_id, ingredient_id, amount) VALUES (15, 57, 0);
