package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.GradeProperties;
import ru.otus.quiz.domain.Student;

@Service
public class GradeService {

    private final InputOutputComponent inputOutputComponent;
    private final GradeProperties gradeProperties;

    public GradeService(InputOutputComponent inputOutputComponent, GradeProperties gradeProperties) {
        this.inputOutputComponent = inputOutputComponent;
        this.gradeProperties = gradeProperties;
    }

    public void runGrade(Student student, int quantityCorrectAnswers) {
        String studentName = student.getFirstName() + " " + student.getSecondName();
        if (quantityCorrectAnswers >= gradeProperties.getMinQuantityCorrectAnswersForApprove()) {
            inputOutputComponent.write(studentName + "! " + gradeProperties.getSuccessMessage());
        } else {
            inputOutputComponent.write(studentName + ". " + gradeProperties.getFailMessage());
        }
    }

}
