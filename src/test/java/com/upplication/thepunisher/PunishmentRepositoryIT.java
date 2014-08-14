package com.upplication.thepunisher;

import com.upplication.config.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaTestConfig.class, PunishmentRepository.class})
public class PunishmentRepositoryIT {

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
        assertEquals(punishmentBD.getTitle(), punishment.getTitle());
        assertEquals(punishmentBD.getDescription(), punishment.getDescription());
    }

    @Test
    public void retrieve_list_of_punishments_ordered_by_title() {
        final String letters = "ABCDE";
        final String description = "A description";

        for (int i = 0; i < letters.length(); i++) {
            final String title = Character.toString(letters.charAt(i));

            punishmentRepository.create(title, description);
        }

        List<Punishment> punishments = punishmentRepository.getAll();

        for (int i = 0; i < punishments.size(); i++) {
            final Punishment p = punishments.get(i);

            assertEquals(Character.toString(letters.charAt(i)), p.getTitle());
        }
    }

    @Test
    public void retrieve_list_of_punishments_ordered_by_title_when_inserted_unordered() {
        final String expected = "ABCDE";
        final String letters = "CBEDA";
        final String description = "A description";

        for (int i = 0; i < letters.length(); i++) {
            final String title = Character.toString(letters.charAt(i));

            punishmentRepository.create(title, description);
        }

        List<Punishment> punishments = punishmentRepository.getAll();

        for (int i = 0; i < punishments.size(); i++) {
            final Punishment p = punishments.get(i);

            assertEquals(Character.toString(expected.charAt(i)), p.getTitle());
        }
    }

    @Test
    public void i_can_not_add_a_punishment_with_empty_title() {
        final String title = "";
        final String description = "world";

        Punishment punishment = punishmentRepository.create(title, description);
        assertEquals(null, punishment);
    }

    @Test
    public void i_can_not_add_a_punishment_with_empty_description() {
        final String title = "hello";
        final String description = "";

        Punishment punishment = punishmentRepository.create(title, description);
        assertEquals(null, punishment);
    }

    @Test
    public void i_can_not_add_a_punishment_with_repeated_title() {
        final String title = "hello";
        final String description = "world";

        Punishment punishment = punishmentRepository.create(title, description);
        assertEquals(title, punishment.getTitle());
        assertEquals(description, punishment.getDescription());

        Punishment repeatedPunishment = punishmentRepository.create(title, description);
        assertEquals(null, repeatedPunishment);
    }

    @Test
     public void i_can_not_add_a_punishment_with_a_title_longer_than_100_characters() {
        final StringBuilder title = new StringBuilder("");
        final String description = "hello";

        for (int i = 0; i <= 101; i++) {
            title.append('a');
        }

        Punishment punishment = punishmentRepository.create(title.toString(), description);
        assertEquals(null, punishment);
    }

    @Test
    public void i_can_not_add_a_punishment_with_a_description_longer_than_100_characters() {
        final StringBuilder description = new StringBuilder("");
        final String title = "hello";

        for (int i = 0; i <= 101; i++) {
            description.append('a');
        }

        Punishment punishment = punishmentRepository.create(title, description.toString());
        assertEquals(null, punishment);
    }

    @Test
    public void edit_punishment_with_inexistent_id() {
        final String title = "ham";
        final String description = "asdasd";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentEdited = punishmentRepository.edit(0, title, description);
        assertEquals(null, punishmentEdited);
    }

    @Test
    public void edit_punishment_with_taken_title() {
        final String title = "ham";
        final String description = "asdasd";

        final String title2 = "ham0";
        final String description2 = "asdasdo";

        Punishment punishment = punishmentRepository.create(title, description);
        punishmentRepository.create(title2, description2);

        Punishment punishmentEdited = punishmentRepository.edit(punishment.getId(), title2, description);
        assertEquals(null, punishmentEdited);
    }

    @Test
    public void edit_punishment() {
        final String title = "ham";
        final String description = "asdasd";

        final String title2 = "ham0";
        final String description2 = "asdasdo";

        punishmentRepository.create(title, description);
        Punishment secondPunishment = punishmentRepository.create(title2, description2);

        Punishment punishmentEdited = punishmentRepository.edit(secondPunishment.getId(), title2, description);
        assertEquals(secondPunishment.getId(), punishmentEdited.getId());
        assertEquals(title2, punishmentEdited.getTitle());
        assertEquals(description, punishmentEdited.getDescription());
    }

    @Test
    public void remove_punishment_with_inexistent_id() {
        final String title = "ham";
        final String description = "asdasd";

        punishmentRepository.create(title, description);

        boolean removed = punishmentRepository.remove(0);
        assertFalse(removed);
    }

    @Test
    public void remove_punishment() {
        final String title = "ham";
        final String description = "asdasd";

        Punishment punishment = punishmentRepository.create(title, description);

        boolean removed = punishmentRepository.remove(punishment.getId());
        assertTrue(removed);
    }
}
