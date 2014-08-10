package com.upplication.thepunisher;

import com.upplication.config.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PunishTestConfig.class, PunishmentRepository.class})
public class PunishmentRepositoryIntegrationTest {

    @Autowired
    private PunishmentRepository punishmentRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void create_punishment_with_title_hello_and_description_bye_then_return_object_punishment_with_title_hello_and_description_bye(){

        final String title = "hello";
        final String description = "bye";

        Punishment punishment = punishmentRepository.create(title, description);

        assertEquals(title, punishment.getTitle());
        assertEquals(description, punishment.getDescription());
    }

    @Test
    public void create_punishment_with_title_pepe_and_description_wold_then_return_object_punishment_with_title_pepe_and_description_world(){

        final String title = "pepe";
        final String description = "world";

        Punishment punishment = punishmentRepository.create(title, description);

        assertEquals(title, punishment.getTitle());
        assertEquals(description, punishment.getDescription());
    }

    @Test
    public void create_punishment_then_persist_in_bd(){

        final String title = "ham";
        final String description = "asdasd";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), title);
        assertEquals(punishmentBD.getDescription(), description);
    }

    @Test
    public void create_another_punishment_then_persist_in_bd(){

        final String title = "another title";
        final String description = "description";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), title);
        assertEquals(punishmentBD.getDescription(), description);
    }
}
