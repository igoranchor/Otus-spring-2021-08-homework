package ru.otus.quiz.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WelcomeProperties {

    @Value("${welcome.message}")
    private String welcomeMessage;

    @Value("${welcome.firstname-message}")
    private String welcomeFirstNameMessage;

    @Value("${welcome.lastname-message}")
    private String welcomeLastNameMessage;

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getWelcomeFirstNameMessage() {
        return welcomeFirstNameMessage;
    }

    public String getWelcomeLastNameMessage() {
        return welcomeLastNameMessage;
    }

    public String getStartTestMessage() {
        return startTestMessage;
    }

    @Value("${welcome.start-message}")
    private String startTestMessage;

}
