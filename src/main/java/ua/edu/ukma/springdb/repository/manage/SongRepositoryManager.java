package ua.edu.ukma.springdb.repository.manage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import ua.edu.ukma.springdb.entity.*;
import org.springframework.stereotype.Repository;
import ua.edu.ukma.springdb.entity.Album;
import ua.edu.ukma.springdb.entity.Song;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SongRepositoryManager {

    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    SongRepositoryManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Double JPQLAverageSongDuration() {
        TypedQuery<Double> query = entityManager.createQuery("select AVG(s.duration) from Song s", Double.class);
        return query.getSingleResult();
    }

    public Song JPQLSongWithMinimumDuration() {
        TypedQuery<Song> query = entityManager.createQuery("select s from Song s where s.duration = (select min(s2.duration) from Song s2)", Song.class);
        return query.getSingleResult();
    }

    public List<Song> CriteriaSongsFromAlbum(String albumName){
        CriteriaQuery<Song> criteriaQuery = criteriaBuilder.createQuery(Song.class);
        Root<Song> root = criteriaQuery.from(Song.class);

        Join<Song, Album> albumJoin = root.join(Song_.album, JoinType.INNER);
        Predicate predicate = criteriaBuilder.equal(albumJoin.get(Album_.title), albumName);
        criteriaQuery.where(predicate);
        TypedQuery<Song> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Song> CriteriaSortedSongsByName(){
        CriteriaQuery<Song> criteriaQuery = criteriaBuilder.createQuery(Song.class);
        Root<Song> root = criteriaQuery.from(Song.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Song_.title)));
        TypedQuery<Song> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Song> CriteriaSongsByDurationMoreThanAndTitleStartsWith(Integer duration, String titleStart){
        CriteriaQuery<Song> criteriaQuery = criteriaBuilder.createQuery(Song.class);
        Root<Song> root = criteriaQuery.from(Song.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(criteriaBuilder.like(root.get(Song_.title), titleStart));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Song_.duration), duration));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Song> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

