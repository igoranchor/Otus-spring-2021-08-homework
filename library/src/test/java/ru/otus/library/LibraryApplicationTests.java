package ru.otus.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.dao.AbstractPostgreSQLContainerTest;

@SpringBootTest
class LibraryApplicationTests extends AbstractPostgreSQLContainerTest {

    @Test
    void contextLoads() {
    }

}
