package ru.otus.library.migration;

import io.mongock.api.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.library.domain.*;

import java.util.List;
import java.util.UUID;

@ChangeUnit(id = "mongo-initializer", order = "1", author = "a.yatskevich")
@RequiredArgsConstructor
public class LibraryInitializer {

    private static final String AUTHOR_COLLECTION_NAME = "authors";
    private static final String BOOK_COLLECTION_NAME = "books";
    private static final Author AUTHOR_PUSHKIN;
    private static final Author AUTHOR_DUMAS;
    private static final Author AUTHOR_TOLKIEN;
    private static final Genre GENRE_NOVEL;
    private static final Genre GENRE_FANTASY;
    private static final Comment COMMENT_FIRST_EUGENE_ONEGIN;
    private static final Comment COMMENT_FIRST_THE_LORN_OF_THE_RINGS;
    private static final Comment COMMENT_SECOND_THE_LORN_OF_THE_RINGS;
    private static final Book BOOK_THE_THREE_MUSKETEERS;
    private static final Book BOOK_EUGENE_ONEGIN;
    private static final Book BOOK_THE_LORN_OF_THE_RINGS;

    static {
        AUTHOR_PUSHKIN = new Author("A.S. Pushkin");
        AUTHOR_DUMAS = new Author("A. Dumas");
        AUTHOR_TOLKIEN = new Author("J.R.R. Tolkien");

        GENRE_NOVEL = new Genre("Novel");
        GENRE_FANTASY = new Genre("Fantasy");

        COMMENT_FIRST_EUGENE_ONEGIN = new Comment(UUID.randomUUID().toString(), "Just best Russian novel");
        COMMENT_FIRST_THE_LORN_OF_THE_RINGS = new Comment(UUID.randomUUID().toString(), "Tolkien - the genius");
        COMMENT_SECOND_THE_LORN_OF_THE_RINGS = new Comment(UUID.randomUUID().toString(), "I think this is not the best book by Tolkien");

        BOOK_EUGENE_ONEGIN = new Book("Eugene Onegin",
                AUTHOR_PUSHKIN, GENRE_NOVEL,
                List.of(COMMENT_FIRST_EUGENE_ONEGIN));
        BOOK_THE_THREE_MUSKETEERS = new Book("The three musketeers",
                AUTHOR_DUMAS, GENRE_NOVEL);
        BOOK_THE_LORN_OF_THE_RINGS = new Book("The lord of the rings",
                AUTHOR_TOLKIEN, GENRE_FANTASY,
                List.of(COMMENT_FIRST_THE_LORN_OF_THE_RINGS, COMMENT_SECOND_THE_LORN_OF_THE_RINGS));
    }

    private final MongoTemplate mongoTemplate;

    @RollbackBeforeExecution
    public void rollbackBefore() {
        mongoTemplate.dropCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
    }

    @Execution
    public void migrationMethod() {
        mongoTemplate.save(AUTHOR_PUSHKIN.setId(UUID.randomUUID().toString()));
        mongoTemplate.save(AUTHOR_DUMAS.setId(UUID.randomUUID().toString()));
        mongoTemplate.save(AUTHOR_TOLKIEN.setId(UUID.randomUUID().toString()));
        mongoTemplate.save(BOOK_THE_THREE_MUSKETEERS.setId(UUID.randomUUID().toString()));
        mongoTemplate.save(BOOK_EUGENE_ONEGIN.setId(UUID.randomUUID().toString()));
        mongoTemplate.save(BOOK_THE_LORN_OF_THE_RINGS.setId(UUID.randomUUID().toString()));
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
    }

}
