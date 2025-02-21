
package ua.edu.ukma.springdb.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.SongRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public Optional<Song> updateSong(Long id, Song updatedSong) {
        return songRepository.findById(id).map(existingSong -> {
            existingSong.setTitle(updatedSong.getTitle());
            existingSong.setAuthor(updatedSong.getAuthor());
            existingSong.setDuration(updatedSong.getDuration());
            existingSong.setAlbum(updatedSong.getAlbum());
            return songRepository.save(existingSong);
        });
    }

    public boolean deleteSong(Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
