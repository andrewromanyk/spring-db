package ua.edu.ukma.springdb.redis;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.edu.ukma.springdb.entity.redis.Author;
import ua.edu.ukma.springdb.entity.redis.Label;
import ua.edu.ukma.springdb.entity.redis.Song;
import ua.edu.ukma.springdb.repository.SongRepository;

@DataRedisTest
@Testcontainers
class RedisRepoTest {

    @Autowired
    private SongRepository songRepository;

    @Container
    @ServiceConnection
    static RedisContainer redisContainer = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        System.out.println("\n\n=== REDIS CONNECTION INFO ===");
        System.out.println("Host: " + redisContainer.getHost());
        System.out.println("Port: " + redisContainer.getFirstMappedPort());
        System.out.println("===============================\n\n");
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }


    @BeforeEach
    void beforeAll() {
        songRepository.deleteAll();
        Label lab =  new Label(null, "Some label");
        Author auth = new Author(null, "AC/DC", lab);
        songRepository.save(new Song(null, "D4C", auth));
    }

    @Test
    void findByTitleTest() {
        Song s = songRepository.findByTitle("D4C").orElse(null);
        System.out.println(s);
        Assertions.assertNotNull(s);
    }

    @Test
    void findByAuthorNameTest() {
        Assertions.assertEquals(1, songRepository.findByAuthorName("AC/DC").size());
    }

    @Test
    void findByAuthorLabelNameTest() {
        Assertions.assertEquals(1, songRepository.findByAuthorLabelName("Some label").size());
    }
}
