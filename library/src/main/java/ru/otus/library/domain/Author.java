package ru.otus.library.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Data
@Table(name = "authors")
@Entity
@ToString(exclude = "books")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "books")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    public Author(String name) {
        this.name = name;
    }

}
