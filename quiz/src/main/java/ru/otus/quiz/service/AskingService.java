package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;
import ru.otus.quiz.config.properties.AskingProperties;
import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;

import java.util.List;

@Service
public class AskingService {

    private final AskingProperties askingProperties;
    private final QuestionDao questionDao;
    private final InputOutputComponent inputOutputComponent;

    public AskingService(QuestionDao questionDao, InputOutputComponent inputOutputComponent,
                         AskingProperties askingProperties) {
        this.questionDao = questionDao;
        this.inputOutputComponent = inputOutputComponent;
        this.askingProperties = askingProperties;
    }

    public int runAsk() {
        int countCorrectAnswers = 0;
        List<Question> questions;
        questions = questionDao.getQuestions();
        for (Question question : questions) {
            if (askQuestion(question)) {
                countCorrectAnswers++;
            }
        }
        return countCorrectAnswers;
    }

    private boolean askQuestion(Question question) {
        int answer = 0;
        boolean mismatch = true;
        inputOutputComponent.write(askingProperties.getQuestionIntro());
        inputOutputComponent.write(String.format(askingProperties.getQuestionMessage(), question.getSerialNumber(), question.getQuestion()));
        inputOutputComponent.write(String.format(askingProperties.getQuestionAnswersMessage(), question.getAnswers()));
        while (mismatch) {
            try {
                inputOutputComponent.write(String.format(askingProperties.getQuestionIndexAnswerMessage(), question.getAnswers().size()));
                answer = Integer.parseInt(inputOutputComponent.read());
                if (answer < 1 || question.getAnswers().size() < answer) {
                    inputOutputComponent.write(askingProperties.getMismatchErrorMessage());
                    continue;
                }
                mismatch = false;
            } catch (NumberFormatException e) {
                inputOutputComponent.write(askingProperties.getMismatchErrorMessage());
            }
        }
        inputOutputComponent.write(askingProperties.getQuestionOutro());
        return question.getCorrectAnswer() == answer;
    }

}
