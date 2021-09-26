package ru.otus.quiz.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AskingProperties {

    @Value("${asking.question-intro}")
    private String questionIntro;

    @Value("${asking.question-outro}")
    private String questionOutro;

    @Value("${asking.question-message}")
    private String questionMessage;

    @Value("${asking.question-answers-message}")
    private String questionAnswersMessage;

    @Value("${asking.question-index-answer-message}")
    private String questionIndexAnswerMessage;

    @Value("${asking.mismatch-error-message}")
    private String mismatchErrorMessage;

    public String getQuestionIntro() {
        return questionIntro;
    }

    public String getQuestionOutro() {
        return questionOutro;
    }

    public String getQuestionMessage() {
        return questionMessage;
    }

    public String getQuestionAnswersMessage() {
        return questionAnswersMessage;
    }

    public String getQuestionIndexAnswerMessage() {
        return questionIndexAnswerMessage;
    }

    public String getMismatchErrorMessage() {
        return mismatchErrorMessage;
    }

}
