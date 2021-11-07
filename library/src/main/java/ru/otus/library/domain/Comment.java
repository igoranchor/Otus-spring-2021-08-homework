package ru.otus.library.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Table(name = "comments")
@Entity
@ToString(exclude = "book")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "book")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.book = book;
    }

}
