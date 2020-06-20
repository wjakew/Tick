/*
programmer Jakub Wawak
all rights reserved
kubawawak@gmail.com
version (from schema) v1.0
sql script makes tables for tick database
*/	
-- table tick_done stores info about done tick objects (1)
CREATE TABLE TICK_DONE
(
tick_done_id INT AUTO_INCREMENT PRIMARY KEY,
tick_done_date VARCHAR(40),
tick_done_duration VARCHAR(40),
tick_done_note VARCHAR(100)	
);
-- table address for storing addresses of places and owners (1.5)
CREATE TABLE ADDRESS
(
address_id INT AUTO_INCREMENT PRIMARY KEY,
address_city VARCHAR(30),
address_street VARCHAR (30),
address_house_number INT,
address_flat_number INT,
address_postal VARCHAR(15),
address_country VARCHAR(30)
);
-- table owner stores all user info
CREATE TABLE OWN
(
owner_id INT AUTO_INCREMENT PRIMARY KEY,
owner_login VARCHAR(30),
address_id INT,
owner_password VARCHAR(72),
owner_name VARCHAR(70),
owner_surname VARCHAR(70),
owner_email_address VARCHAR(60),
owner_age INT,
owner_status INT,
CONSTRAINT fk_own FOREIGN KEY (address_id) REFERENCES ADDRESS(address_id)
);
-- table place stores all user places (3)
CREATE TABLE PLACE
(
place_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
place_name VARCHAR(30),
address_id INT,
CONSTRAINT fk_place FOREIGN KEY (address_id) REFERENCES ADDRESS(address_id),
CONSTRAINT fk_place2 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
-- table tag stores containers for tags (4)
CREATE TABLE HASHTAG_TABLE
(
hashtag_table_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
hashtag_table_name VARCHAR(45),
hashtag_table_note VARCHAR(100),
CONSTRAINT fk_hshtable2 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
-- table tag stores tags (5)
CREATE TABLE TAG
(
tag_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
hashtag_table_id INT,
tag_name VARCHAR(45),
tag_note VARCHAR(100),
CONSTRAINT fk_tag FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
CONSTRAINT fk_tag2 FOREIGN KEY (hashtag_table_id) REFERENCES HASHTAG_TABLE(hashtag_table_id)
);
-- table category stores all categories (6)
CREATE TABLE CATEGORY
(
category_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
category_name VARCHAR(45),
category_note VARCHAR(100),
CONSTRAINT fk_category FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
-- table note stores descryption of the ticks (7)
CREATE TABLE NOTE
(
note_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
note_content VARCHAR(100),
setting1 VARCHAR(40),
setting2 VARCHAR(40),
setting3 VARCHAR(40),
CONSTRAINT fk_note FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
-- table scene stores all of the custom scenes made by user (8)
CREATE TABLE SCENE
(
scene_id INT AUTO_INCREMENT PRIMARY KEY,
hashtag_table_id INT,
place_id INT,
owner_id INT,
category_id INT,
scene_name VARCHAR(30),
scene_note VARCHAR(100),
CONSTRAINT fk_scene FOREIGN KEY (hashtag_table_id) REFERENCES HASHTAG_TABLE(hashtag_table_id),
CONSTRAINT fk_scene2 FOREIGN KEY (place_id) REFERENCES PLACE(place_id),
CONSTRAINT fk_scene3 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
CONSTRAINT fk_scene4 FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id)
);
-- table configuration stores configuration of the program (9)
CREATE TABLE CONFIGURATION
(
configuration_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
sum_entries INT,
debug INT,
conf2 VARCHAR(40),
conf3 VARCHAR(40),
conf4 VARCHAR(40),
conf5 VARCHAR(40),
conf6 VARCHAR(40),
conf7 VARCHAR(40),
CONSTRAINT fk_configuration FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
-- table stores all tick info
CREATE TABLE TICK
(
tick_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
place_id INT,
category_id INT,
note_id INT,
hashtag_table_id INT,
tick_done_id INT,
tick_done_start VARCHAR(60),
tick_date_end VARCHAR(60),
tick_name VARCHAR(60),
CONSTRAINT fk_tick FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
CONSTRAINT fk_tick1 FOREIGN KEY (place_id) REFERENCES PLACE(place_id),
CONSTRAINT fk_tick2 FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id),
CONSTRAINT fk_tick3 FOREIGN KEY (note_id) REFERENCES NOTE(note_id),
CONSTRAINT fk_tick4 FOREIGN KEY (hashtag_table_id) REFERENCES HASHTAG_TABLE(hashtag_table_id),
CONSTRAINT fk_tick5 FOREIGN KEY (tick_done_id) REFERENCES TICK_DONE(tick_done_id)
);
-- end of the file