review = LOAD 'CloudProject/yelp/review.json' USING JsonLoader('review_id:chararray, user_id:chararray, business_id:chararray, stars:int, date:chararray, text:chararray, useful:int, funny:int, cool:int') AS (review_id:chararray, user_id:chararray, business_id:chararray, review_stars:int, date:chararray, text:chararray, useful:int, funny:int, cool:int);

business = LOAD 'CloudProject/yelp/business.json' USING JsonLoader('business_id:chararray, name:chararray, neighborhood:chararray, address:chararray, city:chararray, state:chararray, postalcode:chararray, latitude:float, longitude:float, stars:float, review_count:int, is_open:int, attrubitues: {(chararray)},categories: {(chararray)}, hours: {(chararray)}') AS (business_id:chararray, business_name:chararray, business_neighborhood:chararray, address:chararray, business_city:chararray, business_state:chararray, postalcode:chararray, latitude:float, longitude:float, business_stars:float, business_review_count:int, is_open:int, attrubitues: {(chararray)},categories: {(chararray)}, hours: {(chararray)});

user = LOAD 'CloudProject/yelp/user.json' USING JsonLoader('user_id:chararray, name:chararray, review_count:int, yelping_since:chararray, friends:bag{(friend:chararray)}, useful:int, funny:int, cool:int, fans:int, elite:bag{(year:int)}, average_stars:float, compliment_hot: int, compliment_more:int, compliment_profile:int, compliment_cute: int, compliment_list:int, compliment_note:int, compliment_plain:int, compliment_cool:int, compliment_funny:int, compliment_writer:int, compliment_photos:int') AS (user_id:chararray, user_name:chararray, user_review_count:int, yelping_since:chararray, friends:bag{(friend:chararray)}, useful:int, funny:int, cool:int, user_fans:int, elite:bag{(year:int)}, user_average_stars:float, compliment_hot: int, compliment_more:int, compliment_profile:int, compliment_cute: int, compliment_list:int, compliment_note:int, compliment_plain:int, compliment_cool:int, compliment_funny:int, compliment_writer:int, compliment_photos:int);

rb = JOIN review BY business_id, business BY business_id;

rbu = JOIN rb by review::user_id, user by user_id;

mydata = FOREACH rbu GENERATE review::review_id, review::review_stars, user::user_review_count, COUNT(user::friends) as user_friends, user::user_fans, COUNT(user::elite) as user_elite_years, user::user_average_stars, business::business_city, business::business_stars, business::business_review_count;

STORE mydata INTO 'CloudProject/pigoutput/myoutput' using PigStorage('|');
