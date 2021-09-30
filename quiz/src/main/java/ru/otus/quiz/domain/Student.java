package ru.otus.quiz.domain;

public class Student {

    private final String firstName;
    private final String lastName;

    public Student(String firstName, String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return lastName;
    }

}
