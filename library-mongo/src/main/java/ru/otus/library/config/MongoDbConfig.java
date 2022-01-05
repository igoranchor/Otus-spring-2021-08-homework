package ru.otus.library.config;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.library.repository")
public class MongoDbConfig {
}
