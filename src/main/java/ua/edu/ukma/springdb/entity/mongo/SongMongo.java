package ua.edu.ukma.springdb.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongMongo {
    private String id;
    private String title;
    private String artist;
    private Double duration;

}
