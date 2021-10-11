package ru.otus.quiz.messages;

import org.springframework.stereotype.Component;
import ru.otus.quiz.component.InternationalizeComponent;

@Component
public class GradeMessages {

    private static final String SUCCESS_MESSAGE = "grade.success-message";
    private static final String FAIL_MESSAGE = "grade.fail-message";

    private final InternationalizeComponent internationalizeComponent;

    public GradeMessages(InternationalizeComponent internationalizeComponent) {
        this.internationalizeComponent = internationalizeComponent;
    }

    public String getSuccessMessage() {
        return internationalizeComponent.internationalize(SUCCESS_MESSAGE);
    }

    public String getFailMessage() {
        return internationalizeComponent.internationalize(FAIL_MESSAGE);
    }

}
