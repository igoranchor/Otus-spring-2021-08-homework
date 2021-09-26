package ru.otus.quiz.service;

import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;

import java.util.List;

public class QuizService {

    private final QuestionDao questionDao;

    public QuizService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public void runQuiz() {
        List<Question> questions;
        questions = questionDao.getQuestions();
        for (Question question : questions) {
            printQuestion(question);
        }
    }

    private void printQuestion(Question question) {
        System.out.println("---->>");
        System.out.println(String.format("Question # %d: %s", question.getSerialNumber(), question.getQuestion()));
        System.out.println(String.format("Answers: %s", question.getAnswers()));
        System.out.println("----<<");
    }

}
