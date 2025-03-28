package ua.edu.ukma.springdb.index;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.service.AuthorService;

import java.sql.SQLException;
import java.time.LocalDate;

@SpringBootTest
class IndexTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void index() {
        Author author = new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1));
        Author author2 = new Author("AC/DC", "Rock band",  LocalDate.of(1973, 11, 1));
        Author author3 = new Author("The Rolling Stones", "Rock",  LocalDate.of(1973, 11, 1));

        authorService.createAuthor(author);
        authorService.createAuthor(author3);

        Assertions.assertThrows(DataIntegrityViolationException.class,  () -> {
            authorService.createAuthor(author2);
        });
    }
}
