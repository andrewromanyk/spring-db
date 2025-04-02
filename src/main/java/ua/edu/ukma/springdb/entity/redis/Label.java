package ua.edu.ukma.springdb.entity.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("label")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label {
    @Id
    String id;
    @Indexed
    String name;
}
