package ru.otus.quiz.service;

import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final WelcomeService welcomeService;
    private final AskingService askingService;
    private final GradeService gradeService;

    public QuizService(WelcomeService welcomeService, AskingService askingService, GradeService gradeService) {
        this.welcomeService = welcomeService;
        this.askingService = askingService;
        this.gradeService = gradeService;
    }

    public void runQuiz() {
        var student = welcomeService.welcome();
        var countCorrectAnswers = askingService.runAsk();
        gradeService.runGrade(student, countCorrectAnswers);
    }

}
