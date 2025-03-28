package ua.edu.ukma.springdb.index.mongo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.edu.ukma.springdb.entity.mongo.AlbumMongo;
import ua.edu.ukma.springdb.entity.mongo.SongMongo;
import ua.edu.ukma.springdb.repository.mongo.AlbumMongoRepository;
import ua.edu.ukma.springdb.repository.mongo.SongRepositoryMongo;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MongoTest {

    @Autowired
    private SongRepositoryMongo songRepository;
    @Autowired
    private AlbumMongoRepository albumRepository;

    @Test
    void testSongRepository() {
        SongMongo song = new SongMongo("1", "Bohemian Rhapsody", "Queen", 5.55);
        songRepository.save(song);

        Optional<SongMongo> byTitle = songRepository.findByTitle("Bohemian Rhapsody");
        Optional<SongMongo> byArtist = songRepository.findByArtist("Queen");
        Optional<SongMongo> byDuration = songRepository.findByDuration(5.55);

        Assertions.assertTrue(byTitle.isPresent());
        Assertions.assertTrue(byArtist.isPresent());
        Assertions.assertTrue(byDuration.isPresent());
    }

    @Test
    void testAlbumRepository() {
        AlbumMongo album = new AlbumMongo("1", "A Night at the Opera", "Queen", 12);
        albumRepository.save(album);

        List<AlbumMongo> byArtist = albumRepository.findByArtist("Queen");
        List<AlbumMongo> bySongsCount = albumRepository.findBySongsCount(12);
        Optional<AlbumMongo> byTitle = albumRepository.findByTitle("A Night at the Opera");

        Assertions.assertFalse(byArtist.isEmpty());
        Assertions.assertFalse(bySongsCount.isEmpty());
        Assertions.assertTrue(byTitle.isPresent());
    }
}
