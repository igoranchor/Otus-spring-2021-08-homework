package ru.otus.quiz.controller;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.QuizService;
import ru.otus.quiz.service.WelcomeService;

@ShellComponent
public class ShellService {

    private final WelcomeService welcomeService;
    private final QuizService quizService;

    private Student student;

    public ShellService(WelcomeService welcomeService, QuizService quizService) {
        this.welcomeService = welcomeService;
        this.quizService = quizService;
    }

    @ShellMethod(key = {"welcome", "w"}, value = "This command is required for acquaintance. " +
            "We will not start the quiz until we meet.")
    public void welcome() {
        this.student = welcomeService.welcome();
    }

    @ShellMethod(key = {"start-quiz", "s", "start"}, value = "This command starts the quiz")
    @ShellMethodAvailability(value = "isStudentExists")
    public void startQuiz() {
        quizService.runQuiz(this.student);
    }

    private Availability isStudentExists() {
        return this.student != null ? Availability.available() :
                Availability.unavailable("We will not start the quiz until we meet. Please enter the welcome command");
    }

}
