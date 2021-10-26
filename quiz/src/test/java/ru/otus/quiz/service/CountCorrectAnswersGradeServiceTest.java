package ru.otus.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.GradeProperties;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.messages.GradeMessages;
import ru.otus.quiz.service.impl.CountCorrectAnswersGradeService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {GradeProperties.class, CountCorrectAnswersGradeService.class})
@EnableConfigurationProperties
class CountCorrectAnswersGradeServiceTest {

    private static final String FIRST_NAME = "Сергей";
    private static final String LAST_NAME = "Иванов";
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";
    private static final int QUANTITY_CORRECT_ANSWERS_FOR_SUCCESS = 3;
    private static final int QUANTITY_CORRECT_ANSWERS_FOR_FAIL = 1;

    @Autowired
    private GradeService gradeService;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @MockBean
    private GradeMessages gradeMessages;

    @Captor
    private ArgumentCaptor<String> gradeMessageCaptor;

    @Test
    @DisplayName("Студент ответил на необходимое количество вопросов - успех")
    void successTesting() {
        Student student = new Student(FIRST_NAME, LAST_NAME);
        doNothing().when(inputOutputComponent).write(anyString());
        doReturn(SUCCESS_MESSAGE).when(gradeMessages).getSuccessMessage();

        gradeService.runGrade(student, QUANTITY_CORRECT_ANSWERS_FOR_SUCCESS);

        verify(inputOutputComponent, times(1)).write(gradeMessageCaptor.capture());
        assertTrue(gradeMessageCaptor.getValue().contains(SUCCESS_MESSAGE));
    }

    @Test
    @DisplayName("Студент не ответил на необходимое количество вопросов - провал")
    void failTesting() {
        Student student = new Student(FIRST_NAME, LAST_NAME);
        doNothing().when(inputOutputComponent).write(anyString());
        doReturn(FAIL_MESSAGE).when(gradeMessages).getFailMessage();

        gradeService.runGrade(student, QUANTITY_CORRECT_ANSWERS_FOR_FAIL);

        verify(inputOutputComponent, times(1)).write(gradeMessageCaptor.capture());
        assertTrue(gradeMessageCaptor.getValue().contains(FAIL_MESSAGE));
    }

}
