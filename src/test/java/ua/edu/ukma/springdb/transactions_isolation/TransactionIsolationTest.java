package ua.edu.ukma.springdb.transactions_isolation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.AuthorRepository;
import ua.edu.ukma.springdb.repository.SongRepository;
import ua.edu.ukma.springdb.service.AlbumService;
import ua.edu.ukma.springdb.service.TransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Import({TransactionService.class})
class TransactionIsolationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SongRepository songRepository;

    private Author author;
    private Album album;

    @BeforeEach
    public void setUp() {
        // Clean up any existing data
        songRepository.deleteAll();
        albumRepository.deleteAll();
        authorRepository.deleteAll();

        // Create and save author
        author = new Author();
        author.setName("AC/DC");
        author.setDescription("Group.");
        author.setDateOfCreation(LocalDate.now());
        author = authorRepository.save(author);

        // Create and save album with initial songs
        album = new Album();
        album.setTitle("D4C");
        album.setDescription("Dirty Deeds Done Dirt Cheap");
        album = albumRepository.save(album);

        // Create and save initial songs
        Song song1 = new Song(null, "Dirty Deeds Done Dirt Cheap", author, 195, album);
        Song song2 = new Song(null, "Ain't No Fun", author, 417, album);
        Song song3 = new Song(null, "Big Balls", author, 148, album);

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);

        album.setSongs(new ArrayList<>(List.of(song1, song2, song3)));
    }

    @Test
    public void testReadUncommited() {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Song newSong = new Song(null, "Problem Child", author, 210, album);
            transactionService.saveSong(newSong);
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        int countDuringTransaction = transactionService.songsInAlbumUncommited("D4C");
        System.out.println("Counted");

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        int finalCount = transactionService.songsInAlbumUncommited("D4C");

        Assertions.assertEquals(4, countDuringTransaction);
        Assertions.assertEquals(4, finalCount);
    }

    @Test
    public void testReadCommited() {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Song newSong = new Song(null, "Problem Child", author, 210, album);
            transactionService.saveSong(newSong);
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Counted");
        int countDuringTransaction = transactionService.songsInAlbumCommited("D4C");

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        int finalCount = transactionService.songsInAlbumCommited("D4C");

        Assertions.assertEquals(3, countDuringTransaction);
        Assertions.assertEquals(4, finalCount);
    }

    @Test
    public void testRepeatableRead() {
        List<Integer> lst = new ArrayList<>();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (int i = 0; i <= 10; ++i) {
                lst.add(transactionService.songsInAlbumRepeatable("D4C"));
            }
        });

        Song newSong = new Song(null, "Problem Child", author, 210, album);
        transactionService.saveSong(newSong);

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        int finalCount = transactionService.songsInAlbumCommited("D4C");

        for (int i = 0; i <= 10; ++i) {
            Assertions.assertEquals(3, lst.get(i));
        }

        Assertions.assertEquals(4, finalCount);
    }
}
