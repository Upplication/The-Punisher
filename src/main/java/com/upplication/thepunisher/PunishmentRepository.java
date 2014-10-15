package com.upplication.thepunisher;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PunishmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Punishment create(String title, String description) {

        Punishment punish = new Punishment();

        punish.setTitle(title);
        punish.setDescription(description);

        entityManager.persist(punish);

        return punish;
    }

    public List<Punishment> list() {
        return entityManager.createQuery("FROM Punishment order by title").getResultList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("delete from Punishment").executeUpdate();
    }

    public Punishment getByTitle(String title) {
        try {
            return (Punishment) entityManager.createQuery("FROM Punishment WHERE title = :title")
                .setParameter("title", title)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
    @Transactional
    public void delete(int idPunishment) {
        Punishment punishment = entityManager.find(Punishment.class, idPunishment);
        entityManager.remove(punishment);
    }

    @Transactional
    public Punishment edit(Punishment punishemnt) {

        if (entityManager.find(Punishment.class, punishemnt.getId()) != null){
            return entityManager.merge(punishemnt);
        }
        else{
            throw new PersistenceException("punishemnt#id: " + punishemnt.getId() + " not found");
        }
    }
}
