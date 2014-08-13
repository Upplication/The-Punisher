package com.upplication.thepunisher;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class PunishmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Punishment create(String title, String description) {
        if(!(title == "" || description == "" || title.equals("") || description.equals(""))){
            try{
                entityManager.createQuery("SELECT p FROM Punishment p WHERE p.title = :title")
                        .setParameter("title", title)
                        .getSingleResult();
            }catch(NoResultException e){
                Punishment punish = new Punishment();
                punish.setTitle(title);
                punish.setDescription(description);
                entityManager.persist(punish);
                return punish;
            }
        }
        return null;
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

    @Transactional
    public Punishment  update(Punishment editPunishment){
        return entityManager.merge(editPunishment);
    }
}
