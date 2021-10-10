package ru.otus.quiz.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.quiz.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = QuestionMapper.class)
class QuestionMapperTest {

    private static final String QUESTION_STRING_LINE = "History. What year was serfdom abolished in Russian Empire?;3;1853;1857;1861;1867;";
    private static final int QUESTION_SERIAL_NUMBER = 1;

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    void mapStringLineToQuestionTest() {
        var question = questionMapper.mapStringLineToQuestion(QUESTION_STRING_LINE, QUESTION_SERIAL_NUMBER);
        Question expectedQuestion = new Question(1,
                "History. What year was serfdom abolished in Russian Empire?",
                List.of("1853", "1857", "1861", "1867"),
                3);
        assertEquals(expectedQuestion.getSerialNumber(), question.getSerialNumber());
        assertEquals(expectedQuestion.getQuestion(), question.getQuestion());
        assertEquals(expectedQuestion.getAnswers(), question.getAnswers());
        assertEquals(expectedQuestion.getCorrectAnswer(), question.getCorrectAnswer());
    }

}
