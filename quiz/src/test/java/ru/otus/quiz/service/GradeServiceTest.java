package ru.otus.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.GradeProperties;
import ru.otus.quiz.domain.Student;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    private static final String FIRST_NAME = "Сергей";
    private static final String LAST_NAME = "Иванов";
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String FAIL_MESSAGE = "FAIL";
    private static final int MIN_QUANTITY_CORRECT_ANSWERS_FOR_APPROVE = 3;
    private static final int QUANTITY_CORRECT_ANSWERS_FOR_SUCCESS = 3;
    private static final int QUANTITY_CORRECT_ANSWERS_FOR_FAIL = 1;

    @Mock
    private InputOutputComponent inputOutputComponent;

    @Mock
    private GradeProperties gradeProperties;

    @Captor
    private ArgumentCaptor<String> gradeMessageCaptor;

    @Test
    @DisplayName("Студент ответил на необходимое количество вопросов - успех")
    void successTesting() {
        var student = prepareStudent();
        GradeService service = prepareService();
        doReturn(SUCCESS_MESSAGE).when(gradeProperties).getSuccessMessage();

        service.runGrade(student, QUANTITY_CORRECT_ANSWERS_FOR_SUCCESS);

        verify(inputOutputComponent, times(1)).write(gradeMessageCaptor.capture());
        assertTrue(gradeMessageCaptor.getValue().contains(SUCCESS_MESSAGE));
    }

    @Test
    @DisplayName("Студент не ответил на необходимое количество вопросов - провал")
    void failTesting() {
        var student = prepareStudent();
        GradeService service = prepareService();
        doReturn(FAIL_MESSAGE).when(gradeProperties).getFailMessage();

        service.runGrade(student, QUANTITY_CORRECT_ANSWERS_FOR_FAIL);

        verify(inputOutputComponent, times(1)).write(gradeMessageCaptor.capture());
        assertTrue(gradeMessageCaptor.getValue().contains(FAIL_MESSAGE));
    }

    private GradeService prepareService() {
        doReturn(MIN_QUANTITY_CORRECT_ANSWERS_FOR_APPROVE).when(gradeProperties).getMinQuantityCorrectAnswersForApprove();

        doNothing().when(inputOutputComponent).write(anyString());

        return new GradeService(inputOutputComponent, gradeProperties);
    }

    private Student prepareStudent() {
        return new Student(FIRST_NAME, LAST_NAME);
    }

}
