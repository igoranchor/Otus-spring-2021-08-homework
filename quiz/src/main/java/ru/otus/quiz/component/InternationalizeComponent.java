package ru.otus.quiz.component;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.quiz.config.properties.ApplicationProperties;

import java.util.Locale;

@Component
public class InternationalizeComponent {

    private final MessageSource messageSource;
    private final ApplicationProperties applicationProperties;

    public InternationalizeComponent(MessageSource messageSource, ApplicationProperties applicationProperties) {
        this.messageSource = messageSource;
        this.applicationProperties = applicationProperties;
    }

    public String internationalize(String message) {
        return messageSource
                .getMessage(message, new Object[]{}, Locale.forLanguageTag(applicationProperties.getLanguage()));
    }

}
