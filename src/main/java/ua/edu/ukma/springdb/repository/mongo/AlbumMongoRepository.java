package ua.edu.ukma.springdb.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.edu.ukma.springdb.entity.mongo.AlbumMongo;

import java.util.List;
import java.util.Optional;

public interface AlbumMongoRepository extends MongoRepository<AlbumMongo, Integer> {

    @Query("{'artist':  ?0}")
    List<AlbumMongo> findByArtist(String artist);
    @Query("{'songsCount':  ?0}")
    List<AlbumMongo> findBySongsCount(Integer songsCount);
    @Query("{'title':  ?0}")
    Optional<AlbumMongo> findByTitle(String title);
}
