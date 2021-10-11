package ru.otus.quiz.messages;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.otus.quiz.component.InternationalizeComponent;

@Configuration
@ConfigurationProperties("welcome")
public class WelcomeMessages {

    private static final String MESSAGE = "welcome.message";
    private static final String FIRST_NAME_MESSAGE = "welcome.first-name-message";
    private static final String LAST_NAME_MESSAGE = "welcome.last-name-message";
    private static final String START_MESSAGE = "welcome.start-message";

    private final InternationalizeComponent internationalizeComponent;

    public WelcomeMessages(InternationalizeComponent internationalizeComponent) {
        this.internationalizeComponent = internationalizeComponent;
    }

    public String getMessage() {
        return internationalizeComponent.internationalize(MESSAGE);
    }

    public String getFirstNameMessage() {
        return internationalizeComponent.internationalize(FIRST_NAME_MESSAGE);
    }

    public String getLastNameMessage() {
        return internationalizeComponent.internationalize(LAST_NAME_MESSAGE);
    }

    public String getStartMessage() {
        return internationalizeComponent.internationalize(START_MESSAGE);
    }

}
