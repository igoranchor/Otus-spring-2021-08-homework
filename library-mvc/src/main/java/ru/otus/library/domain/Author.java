package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Accessors(chain = true)
@Document(collection = "authors")
@NoArgsConstructor
public class Author {

    @MongoId
    private String id;

    @Indexed(unique = true)
    private String name;

    public Author(String name) {
        this.name = name;
    }

}
