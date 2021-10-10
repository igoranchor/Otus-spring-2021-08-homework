package ru.otus.quiz.dao.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.mapper.QuestionMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoCsv implements QuestionDao {

    private final String filename;
    private final QuestionMapper questionMapper;

    public QuestionDaoCsv(String filename, QuestionMapper questionMapper) {
        this.filename = filename;
        this.questionMapper = questionMapper;
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
                questions.add(questionMapper.mapStringLineToQuestion(line, index));
                index++;
            }
            bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

}
