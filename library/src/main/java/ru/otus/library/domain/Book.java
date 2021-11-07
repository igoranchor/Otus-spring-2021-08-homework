package ru.otus.library.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Data
@Table(name = "books")
@Entity
@ToString(exclude = "comments")
@NoArgsConstructor
@NamedEntityGraph(name = "author_genre_entity_graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
@EqualsAndHashCode(exclude = "comments")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Genre genre;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Book(String title, Genre genre, Author author) {
        this.title = title;
        this.genre = genre;
        this.author = author;
    }

}

