package ru.otus.quiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.dao.impl.QuestionDaoCsv;

@Configuration
public class QuestionsSourceConfig {

    @Bean
    QuestionDao questionDao(@Value("${questions.source}") String csvFileName) {
        return new QuestionDaoCsv(csvFileName);
    }

}
