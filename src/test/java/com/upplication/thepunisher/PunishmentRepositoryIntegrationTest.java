package com.upplication.thepunisher;

import com.upplication.config.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public void add_two_new_punishments_and_check_the_next_id_is_one_more(){

        final String title = "title1";
        final String description = "description1";

        final String title2 = "title2new";
        final String description2 = "description2new";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        Punishment punishment2 = punishmentRepository.create(title2, description2);

        Punishment punishmentBD2 = entityManager.find(Punishment.class, punishment2.getId());

        assertNotNull(punishmentBD);
        assertNotNull(punishmentBD2);
        assertEquals(punishmentBD.getId()+1, punishmentBD2.getId());
    }


    @Test
    public void add_new_punishment_with_title_and_description_and_check_if_exists_title_on_db(){

        final String title = "titleOnDB";
        final String description = "descriptionOnDB";

        Punishment punishment = punishmentRepository.create(title, description);

        boolean testExists = punishmentRepository.existsTitle(title);

        assertTrue(testExists);

    }

    @Test
    public void add_new_punishment_with_title_and_description_and_check_if_exists_punishment_with_id_on_db(){

        final String title = "titleOnDB2";
        final String description = "descriptionOnDB2";

        Punishment punishment = punishmentRepository.create(title, description);

        boolean testExists = punishmentRepository.existsByID(punishment.getId());

        assertTrue(testExists);

    }

    @Test
    public void add_new_punishment_with_title_and_description_and_check_the_title_is_not_empty(){

        final String title = "";
        final String description = "descriptionOnDB2";

        Punishment punishment = punishmentRepository.create(title, description);

        assertNull(punishment);

    }

    @Test
    public void add_new_punishment_with_title_and_description_and_check_the_description_is_less_than_100_characters(){

        final String title = "";
        //101 characters
        final String description = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        Punishment punishment = punishmentRepository.create(title, description);

        assertNull(punishment);

    }


    @Test
    public void add_new_punishment_with_title_and_description_and_show_a_list_with_the_new_punishment(){

        final String title = "title3";
        //101 characters
        final String description = "description3";

        Punishment punishment = punishmentRepository.create(title, description);

        List<Punishment> punishmentList = punishmentRepository.list();

        System.out.println(punishmentList.get(0).getTitle());
        System.out.println(punishmentList.contains(punishment));

        punishmentRepository.printList();

        assertNotNull(punishmentList);
        assertFalse(punishmentList.isEmpty());
        assertEquals(punishment.getTitle(),punishmentList.get(0).getTitle());
        //assertTrue(punishmentList.contains(punishment));

    }


}
