package ua.edu.ukma.springdb.redis;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.edu.ukma.springdb.entity.redis.Author;
import ua.edu.ukma.springdb.entity.redis.Label;
import ua.edu.ukma.springdb.repository.AuthorService;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
@Import({RedisTestConfiguration.class, AuthorService.class})
class AuthorServiceTest {

    @Autowired
    private RedisContainer redisContainer;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void testSaveAndFindById() {
        // Given
        Label label = new Label("label1", "Rock");
        Author author = new Author("author1", "AC/DC", label);

        // When
        authorService.save(author);
        Author foundAuthor = authorService.findById("author1");

        // Then
        assertNotNull(foundAuthor);
        assertEquals("AC/DC", foundAuthor.getName());
        assertEquals("Rock", foundAuthor.getLabel().getName());
    }

    @Test
    void testDelete() {
        // Given
        Label label = new Label("label2", "Pop");
        Author author = new Author("author2", "Madonna", label);
        authorService.save(author);

        // When
        authorService.delete(author);
        Author foundAuthor = authorService.findById("author2");

        // Then
        assertNull(foundAuthor);
    }

    @Test
    void testFindByIdNonExistent() {
        // When
        Author foundAuthor = authorService.findById("nonexistent");

        // Then
        assertNull(foundAuthor);
    }
}