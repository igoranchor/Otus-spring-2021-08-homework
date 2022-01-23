package ru.otus.library.config;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.otus.library.listener.AuthorCascadeSaveMongoEventListener;

@Configuration
@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.library.repository")
public class MongoDbConfig {

    @Bean
    AuthorCascadeSaveMongoEventListener authorCascadeSaveMongoEventListener() {
        return new AuthorCascadeSaveMongoEventListener();
    }

}
