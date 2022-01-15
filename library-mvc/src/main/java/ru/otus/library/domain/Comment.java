package ru.otus.library.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private String id;

    private String text;

    public Comment(String text) {
        this.text = text;
    }
}
