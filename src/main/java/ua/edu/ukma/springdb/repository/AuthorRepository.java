package ua.edu.ukma.springdb.repository;

import org.springframework.data.repository.CrudRepository;
import ua.edu.ukma.springdb.entity.redis.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
