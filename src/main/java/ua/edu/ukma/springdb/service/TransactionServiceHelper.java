package ua.edu.ukma.springdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.repository.AlbumRepository;

@Service
public class TransactionServiceHelper {

    @Autowired
    private AlbumRepository albumRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public String makeAlbumLowerCaseMandatory(String albumName) {
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setTitle(alb.getTitle().toLowerCase());
        albumRepository.save(alb);
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public String makeAlbumLowerCaseSupports(String albumName) {
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setTitle(alb.getTitle().toLowerCase());
        albumRepository.save(alb);
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NEVER)
    public String makeAlbumLowerCaseNever(String albumName) {
        Album alb = albumRepository.findAlbumByTitle(albumName).orElse(null);
        alb.setTitle(alb.getTitle().toLowerCase());
        albumRepository.save(alb);
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
