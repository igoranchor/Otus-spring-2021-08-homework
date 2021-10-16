truncate table authors cascade;
SELECT setval('s_authors', 1);
insert into authors (name)
values ('Сергей Есенин');
insert into authors (name)
values ('Алексей Иванов');
insert into authors (name)
values ('Владимир Набоков');
commit;