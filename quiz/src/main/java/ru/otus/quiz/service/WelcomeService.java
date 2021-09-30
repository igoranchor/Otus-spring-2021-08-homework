package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.WelcomeProperties;
import ru.otus.quiz.domain.Student;

@Service
public class WelcomeService {

    private final InputOutputComponent inputOutputComponent;
    private final WelcomeProperties welcomeProperties;

    public WelcomeService(InputOutputComponent inputOutputComponent, WelcomeProperties welcomeProperties) {
        this.inputOutputComponent = inputOutputComponent;
        this.welcomeProperties = welcomeProperties;
    }

    public Student welcome() {
        inputOutputComponent.write(welcomeProperties.getWelcomeMessage());
        inputOutputComponent.write(welcomeProperties.getWelcomeFirstNameMessage());
        String studentFirstName = inputOutputComponent.read();
        inputOutputComponent.write(welcomeProperties.getWelcomeLastNameMessage());
        String studentLastName = inputOutputComponent.read();
        inputOutputComponent.write(welcomeProperties.getStartTestMessage());

        return new Student(studentFirstName, studentLastName);
    }

}
