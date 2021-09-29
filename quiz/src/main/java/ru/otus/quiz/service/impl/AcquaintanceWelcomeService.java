package ru.otus.quiz.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.messages.WelcomeMessages;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.WelcomeService;

@Service
public class AcquaintanceWelcomeService implements WelcomeService {

    private final InputOutputComponent inputOutputComponent;
    private final WelcomeMessages welcomeMessages;

    public AcquaintanceWelcomeService(InputOutputComponent inputOutputComponent, WelcomeMessages welcomeMessages) {
        this.inputOutputComponent = inputOutputComponent;
        this.welcomeMessages = welcomeMessages;
    }

    @Override
    public Student welcome() {
        inputOutputComponent.write(welcomeMessages.getMessage());
        inputOutputComponent.write(welcomeMessages.getFirstNameMessage());
        String studentFirstName = inputOutputComponent.read();
        inputOutputComponent.write(welcomeMessages.getLastNameMessage());
        String studentLastName = inputOutputComponent.read();
        inputOutputComponent.write(welcomeMessages.getStartMessage());

        return new Student(studentFirstName, studentLastName);
    }

}
