package ua.edu.ukma.springdb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.repository.manage.AuthorRepositoryManager;

import java.time.LocalDate;

@DataJpaTest
@Import(AuthorRepositoryManager.class)
class AuthorRepositoryManagerTest {

    private AuthorRepositoryManager manager;

    @Autowired
    public void setManager(final AuthorRepositoryManager manager) {
        this.manager = manager;
    }

    @BeforeEach
    void tearDown() {
        manager.deleteAll();
    }

    @Test
    void presistTest() {
        manager.save(new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1)));
        Assertions.assertNotNull(manager.findById(1L));
    }

    @Test
    void detachTest() {
        Author auth = new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1));
        manager.save(auth);
        manager.flush();
        manager.detach(auth);
        Author inDb = manager.findByName("AC/DC");
        Assertions.assertEquals(auth.getAuthorId(), inDb.getAuthorId());
        Assertions.assertNotSame(auth, inDb);
        Assertions.assertEquals(auth.getName(), inDb.getName());
    }

    @Test
    void deleteTest() {
        manager.save(new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1)));
        manager.deleteAll();
        Assertions.assertTrue(manager.findAll().isEmpty());
    }

    @Test
    void refreshTest() {
        Author auth = new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1));
        manager.save(auth);
        manager.flush();
        auth.setName("Led");
        manager.refresh(auth);
        Assertions.assertEquals("AC/DC", auth.getName());
    }

    @Test
    void mergeTest() {
        Author auth = new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1));
        manager.save(auth);
        manager.flush();
        auth.setName("Led");
        manager.merge(auth);
        Author inDb = manager.findByName("Led");
        Assertions.assertEquals("Led", inDb.getName());
    }

    @Test
    void findTest() {
        Author auth = new Author("AC/DC", "Rock",  LocalDate.of(1973, 11, 1));
        Author auth2 = new Author("Led Zeppelin", "Rock",  LocalDate.of(1973, 11, 1));
        manager.save(auth);
        manager.save(auth2);

        Assertions.assertEquals(2, manager.findAll().size());
    }
}
