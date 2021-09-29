package ru.otus.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.quiz.service.QuizService;

@SpringBootApplication
public class QuizApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(QuizApp.class, args);
        QuizService quizService = applicationContext.getBean(QuizService.class);
        quizService.runQuiz();
    }

}
