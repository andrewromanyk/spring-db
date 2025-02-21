package ua.edu.ukma.springdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.springdb.entity.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
