package ua.edu.ukma.springdb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;
import ua.edu.ukma.springdb.entity.redis.Author;

@Service
public class AuthorService {

    private RedisOperations<String, Object> redisOperations;

    @Autowired
    public void setRedisTemplate(RedisOperations<String, Object> redisTemplate) {
        this.redisOperations = redisTemplate;
    }

    public void save(Author author) {
        redisOperations.opsForList().leftPush(author.getId(), author);
    }

    public void  delete(Author author) {
        redisOperations.opsForList().rightPop(author.getId());
    }

    public Author findById(String id) {
        return (Author) redisOperations.opsForList().getFirst(id);
    }


}
