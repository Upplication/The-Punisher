package com.upplication.thepunisher;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PunishmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Punishment create(String title, String description) {
        if (title.equals("") || description.equals("") || existsTitle(title)
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
    public boolean existsTitle(String title) {

        try {
            Punishment punishment = (Punishment)entityManager.createQuery("SELECT p FROM Punishment p WHERE p.title = :title")
                    .setParameter("title", title)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public boolean existsByID(int id) {

        try {
            Punishment punishment = (Punishment)entityManager.createQuery("SELECT p FROM Punishment p WHERE p.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public List<Punishment> list() {

            List<Punishment> list = (List<Punishment>)entityManager.createQuery("SELECT p FROM Punishment p")
                    .getResultList();

            return list;
    }

    @Transactional
    public void printList() {

        List<Punishment> list = (List<Punishment>)entityManager.createQuery("SELECT p FROM Punishment p")
                .getResultList();

        System.out.println(" ----------  PUNISHMENTS --------------- ");
        /*Iterator itr = list.iterator();
        while(itr.hasNext()) {
            Object element = itr.next();

            String title = element.getT

            System.out.print(element. + " ");
        }*/
        for (Punishment element : list) {
            System.out.println("ID: "+element.getId());
            System.out.println("Title: "+element.getTitle());
            System.out.println("Descripcion: "+element.getDescription());
        }


    }

    /*
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Punishment> getAll() {
        return (List<Punishment>) entityManager.createQuery("SELECT p FROM Punishment p ORDER BY p.title ASC").getResultList();
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

        Punishment punishment = null;

        try {
            punishment = (Punishment)entityManager.createQuery("SELECT p FROM Punishment p WHERE p.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
            punishment.setTitle(title);
            punishment.setDescription(description);

            entityManager.merge(punishment);
        } catch (NoResultException ignored) {}

        return punishment;
    }

    @Transactional
    public boolean remove(int id) {
        try {
            Punishment punishment = (Punishment)entityManager.createQuery("SELECT p FROM Punishment p WHERE p.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();

            entityManager.remove(punishment);

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    */
}
