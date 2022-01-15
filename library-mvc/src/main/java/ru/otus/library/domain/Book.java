package ru.otus.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Data
@Accessors(chain = true)
@Document(collection = "books")
@NoArgsConstructor
public class Book {

    @MongoId
    private String id;

    @Indexed(unique = true)
    private String title;

    @DBRef
    private Author author;

    private Genre genre;

    private List<Comment> comments;

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, Author author, Genre genre, List<Comment> comments) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }

}
