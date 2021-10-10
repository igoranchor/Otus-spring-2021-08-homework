package ru.otus.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.dao.impl.QuestionDaoCsv;
import ru.otus.quiz.mapper.QuestionMapper;

@Configuration
public class QuestionsSourceConfig {

    private static final String QUESTION_SOURCE = "question-source";

    private final InternationalizeComponent internationalizeComponent;
    private final QuestionMapper questionMapper;

    public QuestionsSourceConfig(InternationalizeComponent internationalizeComponent, QuestionMapper questionMapper) {
        this.internationalizeComponent = internationalizeComponent;
        this.questionMapper = questionMapper;
    }

    @Bean
    QuestionDao questionDao() {
        return new QuestionDaoCsv(internationalizeComponent.internationalize(QUESTION_SOURCE), questionMapper);
    }

}
