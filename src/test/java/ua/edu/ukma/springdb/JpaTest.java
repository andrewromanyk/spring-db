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

    private Author author = new Author(null, "Led Zeppelin", "British rock band.",
            LocalDate.of(1968, 8, 12));
    private Song song = new Song(null, "Stairway to heaven", author, 482, null);

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();
        authorRepository.deleteAll();
        songRepository.save(song);
    }

    @Test
    void checkSong() {
        Song song1 = songRepository.findAll().get(0);
        Assertions.assertEquals(song, song1);
    }

    @Test
    void checkAuthor() {
        Author author1 = authorRepository.findAll().get(0);
        Assertions.assertEquals(author, author1);
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
