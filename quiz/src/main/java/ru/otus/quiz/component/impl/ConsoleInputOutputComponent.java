package ru.otus.quiz.component.impl;

import org.springframework.stereotype.Service;
import ru.otus.quiz.component.InputOutputComponent;

import java.util.Scanner;

@Service
public class ConsoleInputOutputComponent implements InputOutputComponent {

    private Scanner scanner = new Scanner(System.in);

    public void write(String text) {
        System.out.println(text);
    }

    public String read() {
        return scanner.nextLine();
    }

}
