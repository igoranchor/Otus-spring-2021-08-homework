package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.service.impl.CountCorrectAnswersGradeService;
import ru.otus.quiz.service.impl.QuestionAskingService;

@Service
public class QuizService {

    private final WelcomeService welcomeService;
    private final QuestionAskingService askingService;
    private final GradeService gradeService;

    public QuizService(WelcomeService welcomeService, QuestionAskingService askingService, CountCorrectAnswersGradeService gradeService) {
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
