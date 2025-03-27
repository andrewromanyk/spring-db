package ua.edu.ukma.springdb.JPQL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AuthorRepository;
import ua.edu.ukma.springdb.repository.SongRepository;
import ua.edu.ukma.springdb.repository.manage.AuthorRepositoryManager;
import ua.edu.ukma.springdb.repository.manage.SongRepositoryManager;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@Import({AuthorRepositoryManager.class, SongRepositoryManager.class})
public class JPQLTest {

    @Autowired
    private AuthorRepositoryManager authorRepositoryManager;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SongRepositoryManager songRepositoryManager;

    public Author getAuthor(){
        return new Author(null, "AC/DC", "Australian rock band.",
                LocalDate.of(1973, 11, 1));
    }

    public Author getAuthor2(){
        return new Author(null, "Led Zeppelin", "English rock band.",
                LocalDate.of(1968, 9, 7));
    }

    public Song getSong() {
        Author author = getAuthor();
        return new Song(null, "Highway to Hell", author, 208, null);
    }

    public Song getSecondSong() {
        Author author = getAuthor(); // Use the AC/DC author
        return new Song(null, "Back in Black", author, 255, null);
    }

    @BeforeEach
    public void setUp() {
        authorRepositoryManager.deleteAll();
        authorRepositoryManager.save(getAuthor());
    }

    @Test
    void testDeletion() {
        Assertions.assertEquals(1, authorRepositoryManager.findAll().size());
        authorRepositoryManager.JPQLDeleteByName("AC/DC");
        Assertions.assertEquals(0, authorRepositoryManager.findAll().size());
    }

    @Test
    void testUpdate() {
        Assertions.assertEquals("Australian rock band.", authorRepositoryManager.findAll().get(0).getDescription());
        authorRepositoryManager.JPQLUpdateDescription("AC/DC", "New desc");
//        authorRepositoryManager.flush();
        authorRepositoryManager.clear();
        List<Author> authors = authorRepositoryManager.findAll();
        Assertions.assertEquals("New desc", authors.getFirst().getDescription());
    }

    @Test
    void testAggregated() {
        authorRepositoryManager.save(getAuthor2());
        authorRepositoryManager.flush();
        Assertions.assertEquals(2L, authorRepositoryManager.JPQLAmountOfAuthors());
    }

    @Test
    void testAggregated2() {
        songRepository.save(getSong());
        songRepository.save(getSecondSong());
        songRepository.flush();
        Assertions.assertEquals(songRepositoryManager.JPQLAverageSongDuration(), (getSong().getDuration() + getSecondSong().getDuration())/2.0);
    }

    @Test
    void testAggregated3() {
        songRepository.save(getSong());
        songRepository.save(getSecondSong());
        songRepository.flush();
        Assertions.assertEquals(songRepositoryManager.JPQLSongWithMinimumDuration().getDuration(), 208);
    }
}
