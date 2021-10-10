package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.impl.CountCorrectAnswersGradeService;
import ru.otus.quiz.service.impl.QuestionAskingService;

@Service
public class QuizService {

    private final QuestionAskingService askingService;
    private final GradeService gradeService;

    public QuizService(WelcomeService welcomeService, QuestionAskingService askingService, CountCorrectAnswersGradeService gradeService) {
        this.askingService = askingService;
        this.gradeService = gradeService;
    }

    public void runQuiz(Student student) {
        var countCorrectAnswers = askingService.runAsk();
        gradeService.runGrade(student, countCorrectAnswers);
    }

}
