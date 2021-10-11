package ru.otus.quiz.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuizServiceTest {

    private static final String STUDENT_FIRST_NAME = "Ivan";
    private static final String STUDENT_LAST_NAME = "Ivanov";

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @MockBean
    private QuestionDao questionDao;

    @SpyBean
    private InternationalizeComponent internationalizeComponent;

    @SpyBean
    private WelcomeService welcomeService;

    @SpyBean
    private AskingService askingService;

    @SpyBean
    private GradeService gradeService;

    @Autowired
    private QuizService quizService;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @Test
    void testSuccess() {
        when(questionDao.getQuestions()).thenReturn(prepareQuestions());
        when(inputOutputComponent.read()).thenReturn(STUDENT_FIRST_NAME).thenReturn(STUDENT_LAST_NAME)
                .thenReturn("2").thenReturn("2");
        quizService.runQuiz();

        verify(inputOutputComponent, times(4)).read();
        verify(inputOutputComponent, times(15)).write(anyString());
        verify(internationalizeComponent, times(15)).internationalize(anyString());
        verify(welcomeService, times(1)).welcome();
        verify(askingService, times(1)).runAsk();
        verify(gradeService, times(1)).runGrade(studentArgumentCaptor.capture(), eq(2));
        assertEquals(STUDENT_FIRST_NAME, studentArgumentCaptor.getValue().getFirstName());
        assertEquals(STUDENT_LAST_NAME, studentArgumentCaptor.getValue().getLastName());
    }

    @Test
    void testFail() {
        when(questionDao.getQuestions()).thenReturn(prepareQuestions());
        when(inputOutputComponent.read()).thenReturn(STUDENT_FIRST_NAME).thenReturn(STUDENT_LAST_NAME)
                .thenReturn("2").thenReturn("1");
        quizService.runQuiz();

        verify(inputOutputComponent, times(4)).read();
        verify(inputOutputComponent, times(15)).write(anyString());
        verify(internationalizeComponent, times(15)).internationalize(anyString());
        verify(welcomeService, times(1)).welcome();
        verify(askingService, times(1)).runAsk();
        verify(gradeService, times(1)).runGrade(studentArgumentCaptor.capture(), eq(1));
        assertEquals(STUDENT_FIRST_NAME, studentArgumentCaptor.getValue().getFirstName());
        assertEquals(STUDENT_LAST_NAME, studentArgumentCaptor.getValue().getLastName());
    }

    private List<Question> prepareQuestions() {
        List<String> answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        Question question = new Question(1, "1+1=?", answers, 2);
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        questionList.add(question);

        return questionList;
    }

}