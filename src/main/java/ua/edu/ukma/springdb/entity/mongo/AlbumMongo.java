package ua.edu.ukma.springdb.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class AlbumMongo {

    private String id;
    private String title;
    private String artist;
    private Integer songsCount;

}
