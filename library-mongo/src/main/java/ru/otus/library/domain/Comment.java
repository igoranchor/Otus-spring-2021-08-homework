package ru.otus.library.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Accessors(chain = true)
@Document(collection = "comments")
public class Comment {

    @Id
    private String id = UUID.randomUUID().toString();

    private String text;

    public Comment(String text) {
        this.text = text;
    }

}
