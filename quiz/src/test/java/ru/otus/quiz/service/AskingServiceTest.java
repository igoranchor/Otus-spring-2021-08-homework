package ru.otus.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.messages.AskingMessages;
import ru.otus.quiz.service.impl.QuestionAskingService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = QuestionAskingService.class)
class AskingServiceTest {

    private static final String INTRO_MESSAGE = "---->>";
    private static final String OUTRO_MESSAGE = "----<<";
    private static final String QUESTION_MESSAGE = "QUESTION_MESSAGE";
    private static final String ANSWERS_MESSAGE = "ANSWERS_MESSAGE";
    private static final String INDEX_ANSWER_MESSAGE = "INDEX_ANSWER_MESSAGE";
    private static final String MISMATCH_ERROR_MESSAGE = "MISMATCH_ERROR_MESSAGE";

    @Autowired
    private AskingService askingService;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @MockBean
    private AskingMessages askingMessages;

    @Captor
    private ArgumentCaptor<String> askingMessageCaptor;

    @Test
    @DisplayName("Верный ответ на вопрос с первого раза (без ошибочного ввода)")
    void correctAnswerWithoutIncorrectInput() {
        mockServices();
        doReturn("2").when(inputOutputComponent).read();

        var countCorrectAnswers = askingService.runAsk();

        verify(inputOutputComponent, times(5)).write(askingMessageCaptor.capture());
        verify(inputOutputComponent, times(1)).read();
        assertEquals(1, countCorrectAnswers);
        assertEquals(List.of(INTRO_MESSAGE, QUESTION_MESSAGE,
                ANSWERS_MESSAGE, INDEX_ANSWER_MESSAGE, OUTRO_MESSAGE), askingMessageCaptor.getAllValues());
    }

    @Test
    @DisplayName("Неверный ответ на вопрос с первого раза (без ошибочного ввода)")
    void incorrectAnswerWithoutIncorrectInput() {
        mockServices();
        doReturn("1").when(inputOutputComponent).read();

        var countCorrectAnswers = askingService.runAsk();

        verify(inputOutputComponent, times(5)).write(askingMessageCaptor.capture());
        verify(inputOutputComponent, times(1)).read();
        assertEquals(0, countCorrectAnswers);
        assertEquals(List.of(INTRO_MESSAGE, QUESTION_MESSAGE,
                ANSWERS_MESSAGE, INDEX_ANSWER_MESSAGE, OUTRO_MESSAGE), askingMessageCaptor.getAllValues());
    }

    @Test
    @DisplayName("Верный ответ на вопрос не с первого раза (с ошибочным вводом - не число)")
    void correctAnswerWithIncorrectInput() {
        mockServices();
        when(inputOutputComponent.read()).thenReturn("incorrect_input").thenReturn("2");
        doReturn(MISMATCH_ERROR_MESSAGE).when(askingMessages).getMismatchErrorMessage();

        var countCorrectAnswers = askingService.runAsk();

        verify(inputOutputComponent, times(7)).write(askingMessageCaptor.capture());
        verify(inputOutputComponent, times(2)).read();
        assertEquals(1, countCorrectAnswers);
        assertEquals(List.of(INTRO_MESSAGE, QUESTION_MESSAGE,
                ANSWERS_MESSAGE, INDEX_ANSWER_MESSAGE, MISMATCH_ERROR_MESSAGE,
                INDEX_ANSWER_MESSAGE, OUTRO_MESSAGE), askingMessageCaptor.getAllValues());
    }

    @Test
    @DisplayName("Неверный ответ на вопрос не с первого раза (с ошибочным вводом - нет такого варианта ответа)")
    void incorrectAnswerWithIncorrectInput() {
        mockServices();
        when(inputOutputComponent.read()).thenReturn("incorrect_input").thenReturn("1");
        doReturn(MISMATCH_ERROR_MESSAGE).when(askingMessages).getMismatchErrorMessage();

        var countCorrectAnswers = askingService.runAsk();

        verify(inputOutputComponent, times(7)).write(askingMessageCaptor.capture());
        verify(inputOutputComponent, times(2)).read();
        assertEquals(0, countCorrectAnswers);
        assertEquals(List.of(INTRO_MESSAGE, QUESTION_MESSAGE,
                ANSWERS_MESSAGE, INDEX_ANSWER_MESSAGE, MISMATCH_ERROR_MESSAGE,
                INDEX_ANSWER_MESSAGE, OUTRO_MESSAGE), askingMessageCaptor.getAllValues());
    }

    private void mockServices() {
        doReturn(prepareQuestions()).when(questionDao).getQuestions();

        doReturn(INTRO_MESSAGE).when(askingMessages).getQuestionIntro();
        doReturn(OUTRO_MESSAGE).when(askingMessages).getQuestionOutro();
        doReturn(QUESTION_MESSAGE).when(askingMessages).getQuestionMessage();
        doReturn(ANSWERS_MESSAGE).when(askingMessages).getQuestionAnswersMessage();
        doReturn(INDEX_ANSWER_MESSAGE).when(askingMessages).getQuestionIndexAnswerMessage();
    }

    private List<Question> prepareQuestions() {
        List<String> answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        Question question = new Question(1, "1+1=?", answers, 2);
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);

        return questionList;
    }

}
