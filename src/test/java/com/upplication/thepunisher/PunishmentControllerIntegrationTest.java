package com.upplication.thepunisher;

import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import com.upplication.thepunisher.requests.PunishmentRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentRequest.class})
public class PunishmentControllerIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Inject
    protected PunishmentRepository punishmentRepository;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void post_to_create_punishment_should_throw_error_if_no_data_sent() throws Exception {
        mockMvc.perform(post("/punishment/create"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_should_only_accept_post() throws Exception {
        mockMvc.perform(get("/punishment/create"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_with_invalid_title_length_should_fail() throws Exception {
        mockMvc.perform(post("/punishment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest(sampleString(101), "description"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_with_invalid_description_length_should_fail() throws Exception {
        mockMvc.perform(post("/punishment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest("title", sampleString(101)))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_with_valid_data_should_create_punishment() throws Exception {
        mockMvc.perform(post("/punishment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest("title", "description"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void put_to_edit_punishment_should_throw_error_if_no_data_sent() throws Exception {
        Punishment p = createPunishment("title");
        mockMvc.perform(put("/punishment/" + p.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void put_to_edit_punishment_should_only_accept_put() throws Exception {
        Punishment p = createPunishment("title1");
        mockMvc.perform(get("/punishment/" + p.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void put_to_edit_punishment_with_invalid_title_length_should_fail() throws Exception {
        Punishment p = createPunishment("title2");
        mockMvc.perform(put("/punishment/" + p.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest(sampleString(101), "description"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void put_to_edit_punishment_with_invalid_description_length_should_fail() throws Exception {
        Punishment p = createPunishment("title3");
        mockMvc.perform(put("/punishment/" + p.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest("title", sampleString(101)))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void put_to_edit_punishment_with_valid_data_should_edit_punishment() throws Exception {
        Punishment p = createPunishment("title4");
        mockMvc.perform(put("/punishment/" + p.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(createRequest("edited", "edited"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void delete_to_remove_punishment_should_only_accept_delete() throws Exception {
        Punishment p = createPunishment("title5");
        mockMvc.perform(get("/punishment/" + p.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_to_remove_punishment_with_invalid_id_should_fail() throws Exception {
        mockMvc.perform(delete("/punishment/" + 900)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_to_remove_punishment_with_valid_id_should_remove_punishment() throws Exception {
        Punishment p = createPunishment("title7");
        mockMvc.perform(delete("/punishment/" + p.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Punishment punishment = getPunishment(p.getId());
        assertNull(punishment);
    }

    private String sampleString(int n) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            s.append('c');
        }

        return s.toString();
    }

    private PunishmentRequest createRequest(String title, String description) {
        PunishmentRequest r = new PunishmentRequest();
        r.setTitle(title);
        r.setDescription(description);

        return r;
    }

    @Transactional
    private Punishment createPunishment(String title) {
        return punishmentRepository.create(title, "description");
    }

    @Transactional
    private Punishment getPunishment(int id) {
        return punishmentRepository.get(id);
    }

    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

}
