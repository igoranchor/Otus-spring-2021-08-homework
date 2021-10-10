package ru.otus.quiz.dao.impl;

import org.junit.jupiter.api.Test;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.mapper.QuestionMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionDaoCsvTest {

    private static final int QUESTIONS_SIZE = 2;
    private static final Question QUESTION_FIRST = new Question(1,
            "1+1?",
            List.of("1", "2", "3", "4"),
            2);
    private static final Question QUESTION_SECOND = new Question(2,
            "1+2?",
            List.of("1", "2", "3", "4"),
            3);

    private final QuestionMapper questionMapper = new QuestionMapper();

    @Test
    void questionDaoCsvTest() {
        var dao = prepareDao();
        var questions = dao.getQuestions();

        assertEquals(QUESTIONS_SIZE, questions.size());

        assertEquals(QUESTION_FIRST.getSerialNumber(), questions.get(0).getSerialNumber());
        assertEquals(QUESTION_FIRST.getQuestion(), questions.get(0).getQuestion());
        assertEquals(QUESTION_FIRST.getAnswers(), questions.get(0).getAnswers());
        assertEquals(QUESTION_FIRST.getCorrectAnswer(), questions.get(0).getCorrectAnswer());

        assertEquals(QUESTION_SECOND.getSerialNumber(), questions.get(1).getSerialNumber());
        assertEquals(QUESTION_SECOND.getQuestion(), questions.get(1).getQuestion());
        assertEquals(QUESTION_SECOND.getAnswers(), questions.get(1).getAnswers());
        assertEquals(QUESTION_SECOND.getCorrectAnswer(), questions.get(1).getCorrectAnswer());
    }

    private QuestionDaoCsv prepareDao() {
        return new QuestionDaoCsv("questions.csv", questionMapper);
    }

}
