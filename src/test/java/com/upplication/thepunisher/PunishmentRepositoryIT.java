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
@ContextConfiguration(classes = {JpaTestConfig.class, PunishmentRepository.class})
public class PunishmentRepositoryIT {

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
        assertEquals(punishmentBD.getTitle(), punishment.getTitle());
        assertEquals(punishmentBD.getDescription(), punishment.getDescription());
    }

    @Test
    public void remove_punishment(){

        final String title = "removeMe";
        final String description = "I wanna be removed";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), punishment.getTitle());
        assertEquals(punishmentBD.getDescription(), punishment.getDescription());


        boolean ret = punishmentRepository.remove(punishmentBD.getId());
        assertEquals(ret, true);

    }

    @Test
    public void edit_punishment(){

        final String title = "editMe";
        final String description = "I wanna be edited";

        Punishment punishment = punishmentRepository.create(title, description);

        Punishment punishmentBD = entityManager.find(Punishment.class, punishment.getId());

        assertNotNull(punishmentBD);
        assertEquals(punishmentBD.getId(), punishment.getId());
        assertEquals(punishmentBD.getTitle(), punishment.getTitle());
        assertEquals(punishmentBD.getDescription(), punishment.getDescription());

        Punishment editPunishment = new Punishment();
        editPunishment.setId(punishmentBD.getId());
        editPunishment.setDescription("I am a edited punishment");
        editPunishment.setTitle("Edited");


        Punishment ret = punishmentRepository.update(editPunishment);
        assertNotNull(ret);
        assertEquals(ret.getId(), editPunishment.getId());
        assertEquals(ret.getTitle(), editPunishment.getTitle());
        assertEquals(ret.getDescription(), editPunishment.getDescription());

    }


    @Test
    public void repeated_punishment(){

        final String titleOnce = "I appear Twice";
        final String descriptionOnce = "I wanna appear once";

        punishmentRepository.create(titleOnce, descriptionOnce);

        final String titleTwice = "I appear Twice";
        final String descriptionTwice = "I wanna appear Twice";

        Punishment punishmentTwice = punishmentRepository.create(titleTwice, descriptionTwice);

        assertEquals(punishmentTwice,null);
    }

    @Test
    public void title_empty_punishment(){

        final String titleOnce = "";
        final String descriptionOnce = "I haven't title";

        Punishment punishmentBD = punishmentRepository.create(titleOnce, descriptionOnce);

        assertEquals(punishmentBD,null);
    }

    @Test
    public void description_empty_punishment(){

        final String titleOnce = "I haven't description";
        final String descriptionOnce = "";

        Punishment punishmentBD = punishmentRepository.create(titleOnce, descriptionOnce);

        assertEquals(punishmentBD,null);
    }

    @Test
    public void title_length_100_punishment(){

        final String titleOnce = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final String descriptionOnce = "I haven't title";

        Punishment punishmentBD = punishmentRepository.create(titleOnce, descriptionOnce);

        assertEquals(punishmentBD,null);
    }

    @Test
    public void description_length_100_empty_punishment(){

        final String titleOnce = "I haven't description";
        final String descriptionOnce = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

        Punishment punishmentBD = punishmentRepository.create(titleOnce, descriptionOnce);

        assertEquals(punishmentBD,null);
    }

}
