package ua.edu.ukma.springdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.springdb.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
