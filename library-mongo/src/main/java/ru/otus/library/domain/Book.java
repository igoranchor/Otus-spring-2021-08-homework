package ru.otus.library.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "books")
@ToString(exclude = "comments")
@NoArgsConstructor
public class Book {

    @Id
    private String id = UUID.randomUUID().toString();

    private String title;

    private Author author;

    private Genre genre;

    @DBRef
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
