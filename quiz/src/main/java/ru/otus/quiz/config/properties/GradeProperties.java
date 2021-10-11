package ru.otus.quiz.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("quiz.grade")
public class GradeProperties {

    int minQuantityCorrectAnswersForApprove;

    public void setMinQuantityCorrectAnswersForApprove(int minQuantityCorrectAnswersForApprove) {
        this.minQuantityCorrectAnswersForApprove = minQuantityCorrectAnswersForApprove;
    }

    public int getMinQuantityCorrectAnswersForApprove() {
        return minQuantityCorrectAnswersForApprove;
    }

}
