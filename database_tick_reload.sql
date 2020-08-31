/*
programmer Jakub Wawak
all rights reserved
kubawawak@gmail.com
version (from schema) v1.2.2
sql script reloades tables for tick database
*/
drop table if exists GENERAL_INFO;
drop table if exists SHARE_QUEUE;
drop table if exists TICK;
drop table if exists LISTS;
drop table if exists CONFIGURATION;
drop table if exists SCENE;
drop table if exists NOTE;
drop table if exists CATEGORY;
drop table if exists TAG;
drop table if exists HASHTAG_TABLE;
drop table if exists PLACE;
drop table if exists TICK_DONE;
drop table if exists LOG;
drop table if exists OWN;
drop table if exists ADDRESS;

CREATE TABLE GENERAL_INFO
(
gi_version VARCHAR(20),
gi_data VARCHAR(100),
gi_overview VARCHAR(200),
gi_build_id VARCHAR(100)
);
INSERT INTO GENERAL_INFO
(gi_version,gi_data,gi_overview,gi_build_id)
VALUES
("v1.2.1","09/08/2020","main database relase","12109082020");
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
-- table tick_done stores info about done tick objects (1)
CREATE TABLE TICK_DONE
(
tick_done_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
tick_done_date VARCHAR(40),
tick_done_duration VARCHAR(40),
tick_done_note VARCHAR(100),
CONSTRAINT fk_tid FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
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
conf3 INT,
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
tick_priority INT,
CONSTRAINT fk_tick FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
CONSTRAINT fk_tick1 FOREIGN KEY (place_id) REFERENCES PLACE(place_id),
CONSTRAINT fk_tick2 FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id),
CONSTRAINT fk_tick3 FOREIGN KEY (note_id) REFERENCES NOTE(note_id),
CONSTRAINT fk_tick4 FOREIGN KEY (hashtag_table_id) REFERENCES HASHTAG_TABLE(hashtag_table_id),
CONSTRAINT fk_tick5 FOREIGN KEY (tick_done_id) REFERENCES TICK_DONE(tick_done_id)
);
CREATE TABLE LISTS
(
list_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
tick_list_id VARCHAR(100),
list_name VARCHAR(50),
list_date VARCHAR(50),
CONSTRAINT fk_list FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
CREATE TABLE SHARE_QUEUE
(
share_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
owner_to INT,
tick_id INT,
share_date VARCHAR(50),
share_done VARCHAR(10),
CONSTRAINT fk_share1 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
CONSTRAINT fk_share2 FOREIGN KEY (owner_to) REFERENCES OWN(owner_id),
CONSTRAINT fk_share3 FOREIGN KEY (tick_id) REFERENCES TICK(tick_id)
);
-- table for storing log data from user (12)
CREATE TABLE LOG
(
log_id INT AUTO_INCREMENT PRIMARY KEY,
owner_id INT,
log_string VARCHAR(300),
log_date VARCHAR(50),
CONSTRAINT fk_log FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
);
INSERT INTO ADDRESS
(address_city,address_street,address_house_number,address_flat_number,address_postal,
address_country)
VALUES
('','',-1,-1,'','');

INSERT INTO OWN
(owner_login,address_id,owner_password,owner_name,owner_surname,owner_email_address,
owner_age,owner_status)
VALUES
('nouser',1,'test','noname','nosurname','nomail',0,1);

INSERT INTO OWN
(owner_login,address_id,owner_password,owner_name,owner_surname,owner_email_address,
owner_age,owner_status)
VALUES
('wjakew',1,'test','Jakub','Wawak','kubawawak@gmail.com',24,1);

INSERT INTO OWN
(owner_login,address_id,owner_password,owner_name,owner_surname,owner_email_address,
owner_age,owner_status)
VALUES
('kpaulinek',1,'test','Pauline','Karas','paulinekaras@gmail.com',22,1);

INSERT INTO PLACE
(owner_id,place_name,address_id)
VALUES
(1,'Default place',1);

INSERT INTO HASHTAG_TABLE
(owner_id,hashtag_table_name,hashtag_table_note)
VALUES
(1,'Main Table','Main table for users');

INSERT INTO NOTE
(owner_id,note_content,setting1,setting2,setting3)
VALUES
(1,'No note','','','');

INSERT INTO TAG
(owner_id,hashtag_table_id,tag_name,tag_note)
VALUES
(1,1,'No tags','No tags');

INSERT INTO CATEGORY
(owner_id,category_name,category_note)
VALUES
(1,'Main category','Main category for all of the tick reminders');

INSERT INTO TICK_DONE
(tick_done_date,tick_done_duration,tick_done_note)
VALUES
("no date","no duration","tick done not happen");
-- end of file
