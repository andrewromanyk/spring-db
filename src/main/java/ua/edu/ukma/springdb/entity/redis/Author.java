package ua.edu.ukma.springdb.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("author")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    String id;
    @Indexed
    String name;
    Label label;
}
