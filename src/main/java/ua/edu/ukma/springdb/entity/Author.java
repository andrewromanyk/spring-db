package ua.edu.ukma.springdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "authors", indexes = @Index(columnList = "name", unique = true))
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "author_id")
    private Long authorId;


    private String name;
    private String description;
    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;


    public Author(String name, String description, LocalDate dateOfCreation) {
        this.name = name;
        this.description = description;
        this.dateOfCreation = dateOfCreation;
    }
}
