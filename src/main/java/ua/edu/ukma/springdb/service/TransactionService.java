package ua.edu.ukma.springdb.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Song;
import ua.edu.ukma.springdb.repository.AlbumRepository;
import ua.edu.ukma.springdb.repository.AuthorRepository;
import ua.edu.ukma.springdb.repository.SongRepository;

@Service
public class TransactionService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TransactionServiceHelper  transactionServiceHelper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void capitalizeAllAnnotation(String albumName){
        Album album = albumRepository.findAlbumByTitle(albumName).orElse(null);
        if(album == null){
            throw new RuntimeException("Album Not Found");
        }
        album.setTitle(album.getTitle().toUpperCase());
        if(album.getSongs().size() > 1) {
            throw new RuntimeException("Multiple Songs Found");
        }
        Song song = album.getSongs().getFirst();
        song.setTitle(song.getTitle().toUpperCase());
        albumRepository.save(album);
    }

    public void updateNameAndDesriptionManager(String title, String desription){
        entityManager.getTransaction().begin();
        try {
            Album album = albumRepository.findAlbumByTitle(title).orElse(null);
            album.setTitle(title);
            if (Character.isLowerCase(title.charAt(0))) {
                throw new RuntimeException("Title should start with uppercase");
            }
            album.setDescription(desription);
            albumRepository.save(album);
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            return;
        }
        entityManager.getTransaction().commit();
    }

    public void updateNameAndDesriptionTemplate(String title, String desription) {
        transactionTemplate.execute(status -> {
            Album album = albumRepository.findAlbumByTitle(title).orElse(null);
            album.setTitle(title);
            if (Character.isLowerCase(title.charAt(0))) {
                throw new RuntimeException("Title should start with uppercase");
            }
            album.setTitle(desription);
            albumRepository.save(album);
            return null;
        });
    }

    @Transactional
    public void saveSong(Song song){
        if (song.getAlbum() != null) {
            song.getAlbum().getSongs().add(song);
        }
        songRepository.save(song);
        songRepository.flush();
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("End of adding song");
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Integer songsInAlbumUncommited(String title) {
        Album alb = albumRepository.findAlbumByTitle(title).orElse(null);
        return alb.getSongs().size();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Integer songsInAlbumCommited(String title) {
        Album alb = albumRepository.findAlbumByTitle(title).orElse(null);
        return alb.getSongs().size();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Integer songsInAlbumRepeatable(String title) {
        Album alb = albumRepository.findAlbumByTitle(title).orElse(null);
        return alb.getSongs().size();
    }




    public Boolean makeAlbumDescLowerCaseMandatory(String albumName) {
        String curr = TransactionSynchronizationManager.getCurrentTransactionName();
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setDescription(alb.getDescription().toLowerCase());
        String trans_inner = transactionServiceHelper.makeAlbumLowerCaseMandatory(alb.getTitle());
        albumRepository.save(alb);
        return trans_inner.equals(curr);
    }


    @Transactional
    public Boolean makeAlbumDescLowerCaseSupports(String albumName) {
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setDescription(alb.getDescription().toLowerCase());
        albumRepository.save(alb);
        String trans_inner = transactionServiceHelper.makeAlbumLowerCaseSupports(alb.getTitle());
        return trans_inner.equals(TransactionSynchronizationManager.getCurrentTransactionName());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean makeAlbumDescLowerCaseNever(String albumName) {
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setDescription(alb.getDescription().toLowerCase());
        albumRepository.save(alb);
        String trans_inner = transactionServiceHelper.makeAlbumLowerCaseNever(alb.getTitle());
        return trans_inner.equals(TransactionSynchronizationManager.getCurrentTransactionName());
    }

}
