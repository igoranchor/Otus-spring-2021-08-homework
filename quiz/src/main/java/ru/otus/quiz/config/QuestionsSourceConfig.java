package ru.otus.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.dao.impl.QuestionDaoCsv;

@Configuration
public class QuestionsSourceConfig {

    private static final String QUESTION_SOURCE = "question-source";

    private final InternationalizeComponent internationalizeComponent;

    public QuestionsSourceConfig(InternationalizeComponent internationalizeComponent) {
        this.internationalizeComponent = internationalizeComponent;
    }

    @Bean
    QuestionDao questionDao() {
        return new QuestionDaoCsv(internationalizeComponent.internationalize(QUESTION_SOURCE));
    }

}
