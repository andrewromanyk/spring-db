package ua.edu.ukma.springdb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.springdb.entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
}
