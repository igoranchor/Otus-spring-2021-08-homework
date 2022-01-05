package ru.otus.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

@DataMongoTest
class LibraryMongoApplicationTests {

    @Test
    void contextLoads() {
    }

}
