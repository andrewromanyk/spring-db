package ua.edu.ukma.springdb.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.entity.mongo.SongMongo;

import java.util.Optional;

public interface SongRepositoryMongo extends MongoRepository<SongMongo, Integer> {
    Optional<SongMongo> findByTitle(String title);
    Optional<SongMongo> findByArtist(String artist);
    Optional<SongMongo> findByDuration(Double duration);
}
