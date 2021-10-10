package ru.otus.quiz.mapper;

import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Question;

import java.util.*;

@Component
public class QuestionMapper {

    private static final int INDEX_QUESTION = 0;
    private static final int INDEX_CORRECT_ANSWER = 1;
    private static final String COMMA_DELIMITER = ";";

    public Question mapStringLineToQuestion(String line, int serialNumber) {
        String question = null;
        int correctAnswer = 0;
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(COMMA_DELIMITER);
        List<String> answers = new ArrayList<>();
        int index = 0;
        while (rowScanner.hasNext()) {
            if (index == INDEX_QUESTION) {
                question = rowScanner.next();
            } else if (index == INDEX_CORRECT_ANSWER) {
                correctAnswer = Integer.parseInt(rowScanner.next());
            } else if (index > 6) {
                break;
            } else {
                answers.add(rowScanner.next());
            }
            index++;
        }
        return new Question(serialNumber, question, answers, correctAnswer);
    }

}
