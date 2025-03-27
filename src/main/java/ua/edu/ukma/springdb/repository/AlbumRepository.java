package ua.edu.ukma.springdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.springdb.entity.Album;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findAlbumByTitle(String title);
}
