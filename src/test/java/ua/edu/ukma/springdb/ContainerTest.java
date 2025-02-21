package ua.edu.ukma.springdb;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.SongRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Testcontainers
@DataJpaTest
class ContainerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Author createAuthor() {
        return new Author(null, "AC/DC", "Australian rock band.",
                LocalDate.of(1973, 11, 1));
    }

    private List<Song> createSongs(Author author, Album album) {
        Song song1 = new Song(null, "Dirty Deeds Done Dirt Cheap", author, 195, album);
        Song song2 = new Song(null, "Ain't No Fun (Waiting Round to Be a Millionaire)", author, 417, album);
        Song song3 = new Song(null, "Big Balls", author, 148, album);

        return Arrays.asList(song1, song2, song3);
    }

    private Album createAlbum() {
        Author author = createAuthor();

        Album album = new Album(null, "Dirty Deeds Done Dirt Cheap",
                "D4C",
                null);

        album.setSongs(createSongs(author, album));

        return album;
    }

    @Autowired
    private AlbumRepository aRepository;
    @Autowired
    private SongRepository sRepository;

    @BeforeEach
    void setUp() {
        aRepository.deleteAll();
        aRepository.save(createAlbum());
    }

    @Test
    void testSongRetrieval() {
        Author author = aRepository.findAll().get(0).getAuthors().get(0);
        Assertions.assertEquals(author.getDescription(), createAuthor().getDescription());
    }

    @Test
    void testAlbumRetrieval() {
        List<Author> authors = aRepository.findAll().get(0).getAuthors();
        Assertions.assertEquals(1, authors.size());
    }

    @Test
    void testSongCascadeCreation() {
        Assertions.assertEquals(3, sRepository.findAll().size());
    }

}
