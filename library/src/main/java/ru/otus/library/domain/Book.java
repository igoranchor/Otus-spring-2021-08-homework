package ru.otus.library.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Book {

    private long id;

    @NotBlank(message = "Book title must not be blank")
    private String title;

    @NotNull(message = "Genre must not be null")
    private Genre genre;

    @NotNull(message = "Author must not be null")
    private Author author;

    public Book(String title, Genre genre, Author author) {
        this.title = title;
        this.genre = genre;
        this.author = author;
    }

    public Book(long id, String title, Genre genre, Author author) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre=" + genre.toString() +
                ", author=" + author.toString() +
                '}';
    }
}
