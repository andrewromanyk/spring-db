package ua.edu.ukma.springdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "song_id")
    private Long songId;

    private String title;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(name="author_id", nullable=false)
    private Author author;

    private Integer duration; // seconds

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
}
