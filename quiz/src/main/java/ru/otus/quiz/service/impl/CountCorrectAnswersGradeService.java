package ru.otus.quiz.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.GradeProperties;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.GradeService;

@Service
public class CountCorrectAnswersGradeService implements GradeService {

    private final InputOutputComponent inputOutputComponent;
    private final GradeProperties gradeProperties;

    public CountCorrectAnswersGradeService(InputOutputComponent inputOutputComponent, GradeProperties gradeProperties) {
        this.inputOutputComponent = inputOutputComponent;
        this.gradeProperties = gradeProperties;
    }

    @Override
    public void runGrade(Student student, int quantityCorrectAnswers) {
        String studentName = student.getFirstName() + " " + student.getSecondName();
        if (quantityCorrectAnswers >= gradeProperties.getMinQuantityCorrectAnswersForApprove()) {
            inputOutputComponent.write(studentName + "! " + gradeProperties.getSuccessMessage());
        } else {
            inputOutputComponent.write(studentName + ". " + gradeProperties.getFailMessage());
        }
    }

}
