package ua.edu.ukma.springdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Entity
@Data
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "album_id")
    private Long albumId;

    private String title;
    private String description;

    @OneToMany(mappedBy = "album")
    private List<Song> songs;

    @Transient
    private List<Author> authors;

    public List<Author> getAuthors() {
        if (authors == null) {
            authors = songs.stream()
                    .map(Song::getAuthor)
                    .distinct()
                    .toList();
        }
        return authors;
    }

    public Album(Long albumId, String title, String description, List<Song> songs) {
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.songs = songs;
    }
}
