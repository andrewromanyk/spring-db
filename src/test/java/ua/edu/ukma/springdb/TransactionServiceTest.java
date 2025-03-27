package ua.edu.ukma.springdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.SongRepository;
import ua.edu.ukma.springdb.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Import({TransactionService.class, AlbumService.class, TransactionServiceHelper.class})
public class TransactionServiceTest {

    private Author createAuthor() {
        return new Author(null, "ACDC", "Australian rock band.",
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

        Album album = new Album(null, "d4C",
                "D4C",
                null);

        album.setSongs(createSongs(author, album));

        return album;
    }

    private TransactionService transactionService;
    private AlbumService albumService;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;


    @Autowired
    public void setSongRepository(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setAlbumService(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Autowired
    public void setAlbumRepository(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @BeforeEach
    public void setup(){
        songRepository.deleteAll();
        albumRepository.deleteAll();
    }

    @Test
    public void testAnnotatedTransaction() {
        Album album = albumService.createAlbum(createAlbum());
        Assertions.assertThrows(Exception.class, () ->  transactionService.capitalizeAllAnnotation("d4C"));
        Assertions.assertNotNull(albumRepository.findAlbumByTitle("d4C").orElse(null));
    }

    @Test
    public void testEntityManager() {
        Album album = albumService.createAlbum(createAlbum());
        Assertions.assertThrows(Exception.class, () ->  transactionService.updateNameAndDesriptionManager("D4C", "None"));
        Assertions.assertNotNull(albumRepository.findAlbumByTitle("d4C").orElse(null));
    }

    @Test
    public void testTemplate() {
        Album album = albumService.createAlbum(createAlbum());
        Assertions.assertThrows(Exception.class, () ->  transactionService.updateNameAndDesriptionTemplate("D4C", "None"));
        Assertions.assertNotNull(albumRepository.findAlbumByTitle("d4C").orElse(null));
    }
}
