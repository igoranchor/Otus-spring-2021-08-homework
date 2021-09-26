package ru.otus.quiz.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GradeProperties {

    @Value("${grade.min-quantity-correct-answers-for-approve}")
    int minQuantityCorrectAnswersForApprove;

    @Value("${grade.success-message}")
    private String successMessage;

    @Value("${grade.fail-message}")
    private String failMessage;

    public int getMinQuantityCorrectAnswersForApprove() {
        return minQuantityCorrectAnswersForApprove;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getFailMessage() {
        return failMessage;
    }

}
