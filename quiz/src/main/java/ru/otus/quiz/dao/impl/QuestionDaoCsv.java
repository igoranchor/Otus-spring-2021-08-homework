package ru.otus.quiz.dao.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionDaoCsv implements QuestionDao {

    private static final int INDEX_QUESTION = 0;
    private static final int INDEX_CORRECT_ANSWER = 1;
    private static final String COMMA_DELIMITER = ";";

    final private String filename;

    public QuestionDaoCsv(String filename) {
        this.filename = filename;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        try {
            String line;
            int index = 1;
            Resource resource = new ClassPathResource(filename);
            InputStreamReader streamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            while ((line = bufferedReader.readLine()) != null) {
                questions.add(getQuestionFromLine(line, index));
                index++;
            }
            bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private Question getQuestionFromLine(String line, int serialNumber) {
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(COMMA_DELIMITER);
        Question question = new Question();
        question.setSerialNumber(serialNumber);
        List<String> answers = new ArrayList<>();
        int index = 0;
        while (rowScanner.hasNext()) {
            if (index == INDEX_QUESTION) {
                question.setQuestion(rowScanner.next());
            } else if (index == INDEX_CORRECT_ANSWER) {
                question.setCorrectAnswer(Integer.parseInt(rowScanner.next()));
            } else if (index > 6) {
                break;
            } else {
                answers.add(rowScanner.next());
            }
            index++;
        }
        question.setAnswers(answers);
        return question;
    }

}
