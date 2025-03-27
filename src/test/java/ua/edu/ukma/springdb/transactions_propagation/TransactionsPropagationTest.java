package ua.edu.ukma.springdb.transactions_propagation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.AuthorRepository;
import ua.edu.ukma.springdb.repository.SongRepository;
import ua.edu.ukma.springdb.service.AlbumService;
import ua.edu.ukma.springdb.service.TransactionService;

@SpringBootTest
@Import({TransactionService.class, AlbumService.class})
class TransactionsPropagationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SongRepository songRepository;

    private Album album;

    @BeforeEach
    public void setUp() {
        albumRepository.deleteAll();

        album = new Album();
        album.setTitle("D4C");
        album.setDescription("Dirty Deeds Done Dirt Cheap");
        album = albumRepository.save(album);

    }

    @Test
    public void testRequiresNew() {
        Assertions.assertThrows(IllegalTransactionStateException.class, () -> transactionService.makeAlbumDescLowerCaseMandatory("D4C"));
    }

    @Test
    public void testSupports() {
        Assertions.assertTrue(transactionService.makeAlbumDescLowerCaseSupports("D4C"));
    }

    @Test
    public void testNever() {
        Assertions.assertThrows(IllegalTransactionStateException.class, () -> transactionService.makeAlbumDescLowerCaseNever("D4C"));
    }

}
