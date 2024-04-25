insert into shelter (id, type, address)
values ('1', 'DOG', 'г. Астана, ул. Планерная, д. 1, стр. 1');
insert into shelter (id, type, address)
values ('2', 'CAT', 'г. Астана, ул. Ленина, д. 13, стр. 4');

insert into volunteer  (id, first_name, last_name, phone_number, shelter_id)
values ('1', 'Иван', 'Иванов', '+7-999-999-99-91', '1');
insert into volunteer  (id, first_name, last_name, phone_number, shelter_id)
values ('2', 'Петр', 'Петров', '+7-999-999-99-92', '1');
insert into volunteer  (id, first_name, last_name, phone_number, shelter_id)
values ('3', 'Павел', 'Павлов', '+7-999-999-99-93', '2');
insert into volunteer  (id, first_name, last_name, phone_number, shelter_id)
values ('4', 'Сергей', 'Сергеев', '+7-999-999-99-94', '2');

insert into pet (id, type, status, name, shelter_id)
values ('1', 'DOG', 'ADOPTED', 'Бобик', '1');
insert into pet (id, type, status, name, shelter_id)
values ('2', 'DOG', 'FREE', 'Рекс', '1');
insert into pet (id, type, status, name, shelter_id)
values ('3', 'DOG', 'ADOPTED', 'Джек', '1');
insert into pet (id, type, status, name, shelter_id)
values ('4', 'CAT', 'FREE', 'Рыжик', '2');
insert into pet (id, type, status, name, shelter_id)
values ('5', 'CAT', 'FREE', 'Пушок', '2');
insert into pet (id, type, status, name, shelter_id)
values ('6', 'CAT', 'ADOPTED', 'Снежок', '2');

insert into parent  (id, first_name, last_name, phone_number, pet_id)
values ('1', 'Семен', 'Семенов', '+7-888-888-88-81', '1');
insert into parent  (id, first_name, last_name, phone_number, pet_id)
values ('2', 'Андрей', 'Андреев', '+7-888-888-88-82', '3');
insert into parent  (id, first_name, last_name, phone_number, pet_id)
values ('3', 'Алексей', 'Алексеев', '+7-888-888-88-83', '6');