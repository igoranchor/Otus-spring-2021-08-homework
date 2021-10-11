package ru.otus.quiz.messages;

import org.springframework.stereotype.Component;
import ru.otus.quiz.component.InternationalizeComponent;

@Component
public class AskingMessages {

    private static final String QUESTION_INTRO = "asking.question-intro";
    private static final String QUESTION_OUTRO = "asking.question-outro";
    private static final String QUESTION_MESSAGE = "asking.question-message";
    private static final String QUESTION_ANSWERS_MESSAGE = "asking.question-answers-message";
    private static final String QUESTION_INDEX_ANSWER_MESSAGE = "asking.question-index-answer-message";
    private static final String MISMATCH_ERROR_MESSAGE = "asking.mismatch-error-message";

    private final InternationalizeComponent internationalizeComponent;

    public AskingMessages(InternationalizeComponent internationalizeComponent) {
        this.internationalizeComponent = internationalizeComponent;
    }

    public String getQuestionIntro() {
        return internationalizeComponent.internationalize(QUESTION_INTRO);
    }

    public String getQuestionOutro() {
        return internationalizeComponent.internationalize(QUESTION_OUTRO);
    }

    public String getQuestionMessage() {
        return internationalizeComponent.internationalize(QUESTION_MESSAGE);
    }

    public String getQuestionAnswersMessage() {
        return internationalizeComponent.internationalize(QUESTION_ANSWERS_MESSAGE);
    }

    public String getQuestionIndexAnswerMessage() {
        return internationalizeComponent.internationalize(QUESTION_INDEX_ANSWER_MESSAGE);
    }

    public String getMismatchErrorMessage() {
        return internationalizeComponent.internationalize(MISMATCH_ERROR_MESSAGE);
    }

}
