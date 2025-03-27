package ua.edu.ukma.springdb.repository.manage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.edu.ukma.springdb.entity.Author;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AuthorRepositoryManager {

    private EntityManager em;
    private CriteriaBuilder cb;

    @Autowired
    public AuthorRepositoryManager(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    public void flush() {
        em.flush();
    }

    public void save(Author author) {
        em.persist(author);
    }

    public void detach(Author author) {
        em.detach(author);
    }

    public void delete(Author author) {
        em.remove(em.contains(author) ? author : em.merge(author));
    }

    public void refresh(Author author) {
        em.refresh(author);
    }

    public void merge(Author author) {
        em.merge(author);
    }

    public Author findById(Long id) {
        return em.find(Author.class, id);
    }

    public List<Author> findAll() {
        return em.createQuery("from Author", Author.class).getResultList();
    }


    public void deleteAll() {
        em.createQuery("delete from Author").executeUpdate();
    }

    public Author findByName(String name) {
        return em.createQuery("from Author where name = :name", Author.class).setParameter("name", name).getSingleResult();
    }

    public void clear(){
        em.clear();
    }

    public void JPQLDeleteByName(String name){
        Query query = em.createQuery("delete from Author where name = :name");
        query.setParameter("name", name);
        query.executeUpdate();
    }

    public void JPQLUpdateDescription(String name, String description){
        Query query = em.createQuery("update Author set description = :description where name = :name");
        query.setParameter("description", description);
        query.setParameter("name", name);
        query.executeUpdate();
    }

    public Long JPQLAmountOfAuthors() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Author", Long.class);
        return query.getSingleResult();
    }

    public List<Author> JPQLAuthorsCreatedBeforeDate(LocalDate date){
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.dateOfCreation >= :date", Author.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    public Author CriteriaAuthorByName(String name) {
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);
        Predicate pred = cb.equal(root.get("name"), name);
        query.where(pred);
        TypedQuery<Author> typedQuery = em.createQuery(query);
        return typedQuery.getSingleResult();
    }
}
