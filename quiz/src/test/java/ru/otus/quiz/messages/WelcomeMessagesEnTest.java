package ru.otus.quiz.messages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.quiz.component.InternationalizeComponent;
import ru.otus.quiz.dao.QuestionDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class WelcomeMessagesEnTest {

    @Autowired
    private WelcomeMessages welcomeMessages;

    @MockBean
    @SuppressWarnings("unused")
    private QuestionDao questionDao;

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