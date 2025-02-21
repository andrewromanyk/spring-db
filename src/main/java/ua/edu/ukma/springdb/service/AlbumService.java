package ua.edu.ukma.springdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlbumService {

    private AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    public Optional<Album> updateAlbum(Long id, Album updatedAlbum) {
        return albumRepository.findById(id).map(existingAlbum -> {
            existingAlbum.setTitle(updatedAlbum.getTitle());
            existingAlbum.setDescription(updatedAlbum.getDescription());
            existingAlbum.setSongs(updatedAlbum.getSongs());
            return albumRepository.save(existingAlbum);
        });
    }

    public boolean deleteAlbum(Long id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
