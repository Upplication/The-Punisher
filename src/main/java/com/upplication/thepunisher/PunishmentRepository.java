package com.upplication.thepunisher;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PunishmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Punishment create(String title, String description) {
        if (title.equals("") || description.equals("") || exists(title)
                || title.length() > 100 || description.length() > 100) {
            return null;
        }

        Punishment punishment = new Punishment();

        punishment.setTitle(title);
        punishment.setDescription(description);

        entityManager.persist(punishment);

        return punishment;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Punishment> getAll() {
        return (List<Punishment>) entityManager.createQuery("SELECT p FROM Punishment p ORDER BY p.title ASC").getResultList();
    }

    @Transactional
    public boolean exists(String title) {
        try {
            entityManager.createQuery("SELECT p FROM Punishment p WHERE p.title = :title")
                    .setParameter("title", title)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public boolean exists(String title, int id) {
        try {
            entityManager.createQuery("SELECT p FROM Punishment p WHERE p.title = :title AND p.id <> :id")
                    .setParameter("title", title)
                    .setParameter("id", id)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public Punishment edit(int id, String title, String description) {
        if (title.equals("") || description.equals("") || exists(title, id)
                || title.length() > 100 || description.length() > 100) {
            return null;
        }

        Punishment punishment;

        punishment = (Punishment) entityManager.createQuery("SELECT p FROM Punishment p WHERE p.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        punishment.setTitle(title);
        punishment.setDescription(description);

        entityManager.merge(punishment);

        return punishment;
    }

    @Transactional
    public boolean remove(int id) {
        Punishment punishment = entityManager.find(Punishment.class, id);
        if (punishment != null) {
            entityManager.remove(punishment);
        } else {
            return false;
        }

        return true;
    }

    @Transactional
    public void wipe() {
        entityManager.createQuery("delete from Punishment").executeUpdate();
    }

    @Transactional
    public Punishment get(int id) {
        try {
            return (Punishment) entityManager.createQuery("SELECT p FROM Punishment p WHERE p.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
