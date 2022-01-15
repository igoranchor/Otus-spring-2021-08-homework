package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "authors")
@NoArgsConstructor
public class Author {

    @Id
    private String id = UUID.randomUUID().toString();

    @Indexed(unique = true)
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
