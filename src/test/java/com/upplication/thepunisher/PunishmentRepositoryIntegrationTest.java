package com.upplication.thepunisher;

import com.upplication.config.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishmentRepository.class})
public class PunishmentRepositoryIntegrationTest {

    @Autowired
    private PunishmentRepository punishmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setup(){
        // TODO: how to set transactional this method only and do: entityManager.createQuery("delete from Punishment").executeUpdate();
        punishmentRepository.deleteAll();
    }


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

    // get by title

    @Test
    public void get_by_title_then_return_persisted_title(){
        final String title = "title";

        Punishment expected = punishmentRepository.create(title, "description");
        Punishment actual = punishmentRepository.getByTitle(title);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    public void get_by_title_unknown_then_return_null(){

        Punishment actual = punishmentRepository.getByTitle("");

        assertEquals(null, actual);
    }

    @Test
    public void get_by_title_another_unknown_then_return_null(){

        Punishment actual = punishmentRepository.getByTitle("adadasdad");

        assertEquals(null, actual);
    }

    // list

    @Test
    public void list_persisted_elements(){

        Punishment punishment = punishmentRepository.create("title", "description");

        List<Punishment> persisted = punishmentRepository.list();

        assertNotNull(persisted);
        assertEquals(1, persisted.size());
        Punishment punishmentList = persisted.get(0);
        assertEquals(punishment.getId(), punishmentList.getId());
        assertEquals(punishment.getTitle(), punishmentList.getTitle());
        assertEquals(punishment.getDescription(), punishmentList.getDescription());
    }

    @Test
    public void list_another_persisted_elements(){

        Punishment punishment = punishmentRepository.create("another title", "description");

        List<Punishment> persisted = punishmentRepository.list();

        assertNotNull(persisted);
        assertEquals(1, persisted.size());
        Punishment punishmentList = persisted.get(0);
        assertEquals(punishment.getId(), punishmentList.getId());
        assertEquals(punishment.getTitle(), punishmentList.getTitle());
        assertEquals(punishment.getDescription(), punishmentList.getDescription());
    }

    @Test
    public void list_persisted_elements_return_ordered_alphabetical_by_title(){

        Punishment punishment1 = punishmentRepository.create("another title", "description");
        Punishment punishment2 = punishmentRepository.create("title", "description");

        List<Punishment> persisted = punishmentRepository.list();

        assertNotNull(persisted);
        assertEquals(2, persisted.size());
        assertEquals(punishment1.getId(), persisted.get(0).getId());
        assertEquals(punishment2.getId(), persisted.get(1).getId());
    }

    @Test
    public void another_list_persisted_elements_return_ordered_alphabetical_by_title(){

        Punishment punishment1 = punishmentRepository.create("zzzz", "description");
        Punishment punishment2 = punishmentRepository.create("aaaa", "description");

        List<Punishment> persisted = punishmentRepository.list();

        assertNotNull(persisted);
        assertEquals(2, persisted.size());
        assertEquals(punishment2.getId(), persisted.get(0).getId());
        assertEquals(punishment1.getId(), persisted.get(1).getId());
    }

    @Test
    public void delete_punishment_then_cant_return_with_the_same_id(){

        Punishment punishment1 = punishmentRepository.create("zzzz", "description");

        punishmentRepository.delete(punishment1.getId());

        List<Punishment> persisted = punishmentRepository.list();

        assertEquals(0, persisted.size());
    }

    @Test
    public void edit_title_punishment_then_return_with_the_same_id_and_different_title(){
        Punishment punishment1 = punishmentRepository.create("zzzz", "description");

        Punishment punishmntToEdit = new Punishment();
        punishmntToEdit.setId(punishment1.getId());
        punishmntToEdit.setTitle("new title");
        punishmntToEdit.setDescription(punishment1.getDescription());

        Punishment punishmentEdited = punishmentRepository.edit(punishmntToEdit);

        assertNotNull(punishmentEdited);
        assertEquals(punishment1.getId(), punishmentEdited.getId());
        assertEquals("new title", punishmentEdited.getTitle());
        assertEquals(punishment1.getDescription(), punishmentEdited.getDescription());
    }

    @Test
    public void edit_description_punishment_then_return_with_the_same_id_and_different_description(){
        Punishment punishment1 = punishmentRepository.create("zzzz", "description");

        Punishment punishmntToEdit = new Punishment();
        punishmntToEdit.setId(punishment1.getId());
        punishmntToEdit.setTitle(punishment1.getTitle());
        punishmntToEdit.setDescription("new description");

        Punishment punishmentEdited = punishmentRepository.edit(punishmntToEdit);

        assertNotNull(punishmentEdited);
        assertEquals(punishment1.getId(), punishmentEdited.getId());
        assertEquals(punishment1.getTitle(), punishmentEdited.getTitle());
        assertEquals("new description", punishmentEdited.getDescription());
    }

    @Test
    public void edit_punishemnt_then_get_by_id_return_changed(){
        Punishment punishment1 = punishmentRepository.create("zzzz", "description");

        Punishment punishmntToEdit = new Punishment();
        punishmntToEdit.setId(punishment1.getId());
        punishmntToEdit.setTitle("new title");
        punishmntToEdit.setDescription("new description");

        punishmentRepository.edit(punishmntToEdit);
        Punishment punishmentEdited = punishmentRepository.getByTitle(punishmntToEdit.getTitle());

                assertNotNull(punishmentEdited);
        assertEquals(punishment1.getId(), punishmentEdited.getId());
        assertEquals("new title", punishmentEdited.getTitle());
        assertEquals("new description", punishmentEdited.getDescription());
    }

    @Test(expected = PersistenceException.class)
    public void edit_title_punishment_with_unknown_id_then_throw_exception(){

        Punishment punishmntToEdit = new Punishment();
        punishmntToEdit.setId(12312312);
        punishmntToEdit.setTitle("new title");
        punishmntToEdit.setDescription("adsadasdasdasd");

        punishmentRepository.edit(punishmntToEdit);
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    public void edit_punishemnt_with_same_title_then_throw_exception(){
        Punishment punishment1 = punishmentRepository.create("zzzz", "description");
        Punishment punishment2 = punishmentRepository.create("cccc", "description");

        Punishment punishmntToEdit = new Punishment();
        punishmntToEdit.setId(punishment1.getId());
        punishmntToEdit.setTitle(punishment2.getTitle());
        punishmntToEdit.setDescription("new description");

        punishmentRepository.edit(punishmntToEdit);
    }
    */
}
