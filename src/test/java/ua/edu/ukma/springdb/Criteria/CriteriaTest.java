package ua.edu.ukma.springdb.Criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.SongRepository;
import ua.edu.ukma.springdb.repository.manage.AuthorRepositoryManager;
import ua.edu.ukma.springdb.repository.manage.SongRepositoryManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Import({SongRepositoryManager.class, AuthorRepositoryManager.class})
public class CriteriaTest {

    @Autowired
    private AuthorRepositoryManager authorRepositoryManager;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SongRepositoryManager songRepositoryManager;
    @Autowired
    private AlbumRepository albumRepository;

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

    @BeforeEach
    public void setUp() throws Exception {
        authorRepositoryManager.deleteAll();
        songRepository.deleteAll();
        albumRepository.deleteAll();
        albumRepository.saveAndFlush(createAlbum());
    }

    @Test
    void testByField(){
        Assertions.assertEquals("Australian rock band.", authorRepositoryManager.CriteriaAuthorByName("AC/DC").getDescription());
    }

    @Test
    void testByJoin(){
        List<Song> songs = songRepositoryManager.CriteriaSongsFromAlbum("Dirty Deeds Done Dirt Cheap");
        Assertions.assertEquals(3, songs.size());
        Assertions.assertEquals("Dirty Deeds Done Dirt Cheap", songs.get(0).getTitle());
        Assertions.assertEquals("Ain't No Fun (Waiting Round to Be a Millionaire)", songs.get(1).getTitle());
        Assertions.assertEquals("Big Balls", songs.get(2).getTitle());
    }

    @Test
    void testBySort(){
        List<Song> songs = songRepositoryManager.CriteriaSortedSongsByName();
        Assertions.assertEquals(3, songs.size());
        Assertions.assertEquals("Ain't No Fun (Waiting Round to Be a Millionaire)", songs.get(0).getTitle());
        Assertions.assertEquals("Big Balls", songs.get(1).getTitle());
        Assertions.assertNotEquals("Big Ball", songs.get(1).getTitle());
        Assertions.assertEquals("Dirty Deeds Done Dirt Cheap", songs.get(2).getTitle());
    }

    @Test
    void testByPredicate(){
        List<Song> songs = songRepositoryManager.CriteriaSongsByDurationMoreThanAndTitleStartsWith(190, "A%");
        Assertions.assertEquals(1, songs.size());
        Assertions.assertEquals("Ain't No Fun (Waiting Round to Be a Millionaire)", songs.get(0).getTitle());
    }
}
