## Описание
Домашнее задание на работу с БД

##  Локальный запуск
Необходимо поднять Mongo DB в docker контейнере:

```shell script
docker run --name mongo --rm -e MONGO_INITDB_ROOT_USERNAME=library -e MONGO_INITDB_ROOT_PASSWORD=library -p 27017:27017 -d mongo
```
