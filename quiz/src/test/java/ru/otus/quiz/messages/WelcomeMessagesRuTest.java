package ru.otus.quiz.messages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.dao.QuestionDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(initializers = {WelcomeMessagesRuTest.Initializer.class})
class WelcomeMessagesRuTest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "quiz.application.language=ru-RU"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private WelcomeMessages welcomeMessages;

    @MockBean
    @SuppressWarnings("unused")
    private QuestionDao questionDao;

    @SpyBean
    private InternationalizeComponent internationalizeComponent;

    @Test
    void testMessagesLanguageIsRussian() {
        assertEquals("ПРИВЕТ", welcomeMessages.getMessage());
        assertEquals("ИМЯ", welcomeMessages.getFirstNameMessage());
        assertEquals("ФАМИЛИЯ", welcomeMessages.getLastNameMessage());
        assertEquals("ПОЕХАЛИ", welcomeMessages.getStartMessage());

        verify(internationalizeComponent, times(4)).internationalize(any());
    }

}