package com.upplication.thepunisher;

import com.upplication.config.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishmentRepository.class})
public class PunishmentRepositoryIntegrationTest {

    @Autowired
    private PunishmentRepository punishmentRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void create_punishment_with_title_hello_and_description_bye_then_return_object_punishment_with_title_hello_and_description_bye() {
        final String title = "hello";
        final String description = "bye";

        Punishment punishment = punishmentRepository.create(title, description);

        assertEquals(title, punishment.getTitle());
        assertEquals(description, punishment.getDescription());
    }

    @Test
    public void create_punishment_with_title_pepe_and_description_wold_then_return_object_punishment_with_title_pepe_and_description_world() {
        final String title = "pepe";
        final String description = "world";

        Punishment punishment = punishmentRepository.create(title, description);

        assertEquals(title, punishment.getTitle());
        assertEquals(description, punishment.getDescription());
    }

    @Test
    public void create_punishment_then_persist_in_bd() {
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

    @Test
    public void add_new_punishment_with_title_and_description_and_get_the_next_id(){

        final String title = "title2";
        final String description = "description2";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), title);
        assertEquals(punishmentBD.getDescription(), description);
    }
    
    @Test
    public void add_new_punishment_with_title_and_description_and_get_the_next_id_second_version(){

        final String title = "title2";
        final String description = "description2";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), title);
        assertEquals(punishmentBD.getDescription(), description);
    }

}
