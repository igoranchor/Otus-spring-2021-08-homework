package ru.otus.quiz.domain;

import java.util.List;

public class Question {

    private final int serialNumber;
    private final String question;
    private final List<String> answers;
    private final int correctAnswer;

    public Question(int serialNumber, String question, List<String> answers, int correctAnswer) {
        this.serialNumber = serialNumber;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

}
