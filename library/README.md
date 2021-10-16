## Описание
Домашнее задание на работу с БД

##  Локальный запуск
Необходимо поднять БД в docker контейнере:

```shell script
docker run --name postgres --rm -e POSTGRES_USER=library -e POSTGRES_PASSWORD=library -p 5432:5432 -d postgres
```