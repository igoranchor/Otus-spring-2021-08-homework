package ru.otus.library.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import ru.otus.library.domain.Author;
import ru.otus.library.service.BookService;

public class AuthorCascadeSaveMongoEventListener extends AbstractMongoEventListener<Author> {

    @Autowired
    private BookService bookService;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        bookService.deleteByAuthorId(((String) event.getSource().get("_id")));
    }

}
