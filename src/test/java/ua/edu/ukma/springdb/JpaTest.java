package ua.edu.ukma.springdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AuthorRepository;
import ua.edu.ukma.springdb.repository.SongRepository;

import java.time.LocalDate;


@DataJpaTest
class JpaTest {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private Author createAuthor() {
        return new Author(null, "Led Zeppelin", "British rock band.",
                LocalDate.of(1968, 8, 12));
    }

    private Song createSong() {
        return new Song(null, "Stairway to heaven", createAuthor(), 482, null);
    }

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();
        authorRepository.deleteAll();
        songRepository.flush();
        authorRepository.flush();
        songRepository.save(createSong());
    }

    @Test
    void checkSong() {
        Song song1 = songRepository.findAll().get(0);
        Assertions.assertEquals(createSong().getTitle(), song1.getTitle());
    }

    @Test
    void checkAuthor() {
        Author author1 = authorRepository.findAll().get(0);
        Assertions.assertEquals(createAuthor().getName(), author1.getName());
    }

    @Test
    void checkAuthorChangeAndDynamicFieldChange() {
        Author author1 = authorRepository.findAll().get(0);
        author1.setName("Led Zeppelin1");

        Song song1 = songRepository.findAll().get(0);
        Author author2 = song1.getAuthor();

        Assertions.assertEquals(author1.getName(), author2.getName());
    }
}
