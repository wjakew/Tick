INSERT INTO ADDRESS
(address_city,address_street,address_house_number,address_flat_number,address_postal,
address_country)
VALUES
('','',-1,-1,'','');
INSERT INTO ADDRESS
(address_city,address_street,address_house_number,address_flat_number,address_postal,
address_country)
VALUES
('Plock','Leszczynowa',9,0,'90-473','Poland');

INSERT INTO OWN
(owner_login,address_id,owner_password,owner_name,owner_surname,owner_email_address,
owner_age,owner_status)
VALUES
('wjakew',1,'test','Jakub','Wawak','kubawawak@gmail.com',24,1);
INSERT INTO PLACE
(owner_id,place_name,address_id)
VALUES
(1,'Kawiarnia Testowa',1);

INSERT INTO HASHTAG_TABLE
(owner_id,hashtag_table_name,hashtag_table_note)
VALUES
(1,'Main Table','Main table for users');

INSERT INTO CONFIGURATION
(owner_id,sum_entries,debug,conf2,conf3,conf4,conf5,conf6,conf7)
VALUES
(1,1,1,'','','','','','');

INSERT INTO TAG
(owner_id,hashtag_table_id,tag_name,tag_note)
VALUES
(1,1,'Testowy Tag','Tag do testowania');
INSERT INTO CATEGORY
(owner_id,category_name,category_note)
VALUES
(1,'Main Category','Main category for all of the tick reminders');