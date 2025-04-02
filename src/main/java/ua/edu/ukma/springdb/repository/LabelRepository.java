package ua.edu.ukma.springdb.repository;

import org.springframework.data.repository.CrudRepository;
import ua.edu.ukma.springdb.entity.redis.Label;

import java.util.Optional;

public interface LabelRepository extends CrudRepository<Label, Long> {
}
