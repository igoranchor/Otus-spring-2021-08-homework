package ru.otus.quiz.messages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.config.properties.ApplicationProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EnableConfigurationProperties
class WelcomeMessagesEnTest {

    @Import({ApplicationProperties.class,
            WelcomeMessages.class})
    @Configuration
    static class TestConfiguration {
        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource source = new ResourceBundleMessageSource();
            source.setBasenames("i18n/messages", "i18n/question-source");
            source.setDefaultEncoding("UTF-8");
            return source;
        }
    }

    @Autowired
    private WelcomeMessages welcomeMessages;

    @SpyBean
    private InternationalizeComponent internationalizeComponent;

    @Test
    void testMessagesLanguageIsEnglish() {
        assertEquals("HELLO", welcomeMessages.getMessage());
        assertEquals("FIRST_NAME", welcomeMessages.getFirstNameMessage());
        assertEquals("LAST_NAME", welcomeMessages.getLastNameMessage());
        assertEquals("START", welcomeMessages.getStartMessage());

        verify(internationalizeComponent, times(4)).internationalize(any());
    }

}