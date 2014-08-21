package com.upplication.thepunisher;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
