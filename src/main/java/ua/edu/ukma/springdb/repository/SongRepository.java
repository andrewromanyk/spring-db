package ua.edu.ukma.springdb.repository;


import org.springframework.data.repository.CrudRepository;
import ua.edu.ukma.springdb.entity.redis.Song;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends CrudRepository<Song, String> {
    Optional<Song> findByTitle(String title);
    List<Song> findByAuthorName(String authorName);
    List<Song> findByAuthorLabelName(String authorLabelName);
}
