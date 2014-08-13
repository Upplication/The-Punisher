package com.upplication.thepunisher;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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