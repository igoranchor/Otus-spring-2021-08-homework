package ru.otus.library.component.impl;

import org.springframework.stereotype.Component;
import ru.otus.library.component.InputOutputComponent;

@Component
public class ConsoleInputOutputComponent implements InputOutputComponent {

    public void write(String text) {
        System.out.println(text);
    }

}
