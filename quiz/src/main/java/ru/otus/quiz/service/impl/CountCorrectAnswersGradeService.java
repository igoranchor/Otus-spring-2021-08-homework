package ru.otus.quiz.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.GradeProperties;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.messages.GradeMessages;
import ru.otus.quiz.service.GradeService;

@Service
public class CountCorrectAnswersGradeService implements GradeService {

    private final InputOutputComponent inputOutputComponent;
    private final GradeProperties gradeProperties;
    private final GradeMessages gradeMessages;

    public CountCorrectAnswersGradeService(InputOutputComponent inputOutputComponent, GradeProperties gradeProperties, GradeMessages gradeMessages) {
        this.inputOutputComponent = inputOutputComponent;
        this.gradeProperties = gradeProperties;
        this.gradeMessages = gradeMessages;
    }

    @Override
    public void runGrade(Student student, int quantityCorrectAnswers) {
        String studentName = student.getFirstName() + " " + student.getLastName();
        if (quantityCorrectAnswers >= gradeProperties.getMinQuantityCorrectAnswersForApprove()) {
            inputOutputComponent.write(studentName + "! " + gradeMessages.getSuccessMessage());
        } else {
            inputOutputComponent.write(studentName + ". " + gradeMessages.getFailMessage());
        }
    }

}
