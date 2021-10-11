package ru.otus.quiz.service;

import ru.otus.quiz.domain.Student;

public interface GradeService {

    void runGrade(Student student, int quantityCorrectAnswers);

}
