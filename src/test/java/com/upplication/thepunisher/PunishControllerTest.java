package com.upplication.thepunisher;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PunishControllerTest {

    @InjectMocks
    private PunishmentController controller = new PunishmentController();

    @Mock
    private PunishmentRepository punishmentRepository;

    /*
     * preguntar a esta gente como han resuleto el tema de que este guardado?
     * En mi caso, para poder validar que el punishmentController devuelve un id unico
     * no tengo mas remedio que mockear y por eso hago este test
     */

    @Test
    public void save_punishment_then_return_id() {
        final int id = 1;
        mockPersistPunishmentWithId(id);
        PunishmentForm data = getPunishmentForm();

        PunishmentController.Success success = controller.savePunishment(data);

        verify(punishmentRepository, times(1)).create(eq(data.getTitle()), eq(data.getDescription()));
        assertEquals(id, success.getId());
    }

    @Test
    public void save_punishment_then_return_another_id() {
        final int id = 2;
        mockPersistPunishmentWithId(id);
        PunishmentForm data = getPunishmentForm();

        PunishmentController.Success success = controller.savePunishment(data);

        verify(punishmentRepository, times(1)).create(eq(data.getTitle()), eq(data.getDescription()));
        assertEquals(id, success.getId());
    }

    // list punishment

    @Test
    public void list_punishment_then_return_list_punishment_with_title_and_description() {
        Punishment punishment = new Punishment();
        punishment.setId(1);
        punishment.setDescription("my first punishment");
        punishment.setTitle("hello");
        when(punishmentRepository.list()).thenReturn(Arrays.asList(punishment));

        List<Punishment> list = controller.listPunishment();

        assertEquals(1, list.size());
        Punishment punishmentList = list.get(0);
        assertEquals(1, punishmentList.getId());
        assertEquals("hello", punishmentList.getTitle());
        assertEquals("my first punishment", punishmentList.getDescription());
    }

    @Test
    public void list_punishment_then_return_list_punishment_persisted() {
        Punishment punishment = new Punishment();
        punishment.setId(1);
        punishment.setDescription("description");
        punishment.setTitle("title");
        when(punishmentRepository.list()).thenReturn(Arrays.asList(punishment));

        List<Punishment> list = controller.listPunishment();

        assertEquals(1, list.size());
        assertEquals(punishment, list.get(0));
    }

    //
    // helpers
    //

    private PunishmentForm getPunishmentForm() {
        PunishmentForm data = new PunishmentForm();
        data.setTitle("hello");
        data.setDescription("hello");
        return data;
    }

    private void mockPersistPunishmentWithId(int id) {
        Punishment punishment = new Punishment();
        punishment.setId(id);
        when(punishmentRepository.create(anyString(), anyString())).thenReturn(punishment);
    }
}