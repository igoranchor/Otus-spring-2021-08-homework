truncate table genres cascade;
SELECT setval('s_genres', 1);
insert into genres (name)
values ('фэнтези');
insert into genres (name)
values ('шпионский роман');
insert into genres (name)
values ('басня');
commit;