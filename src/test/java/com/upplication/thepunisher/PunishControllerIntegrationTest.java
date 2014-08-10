package com.upplication.thepunisher;

import com.upplication.config.PunishTestConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentForm.class})
public class PunishControllerIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void post_to_save_punishment_only_accept_post_if_not_then_return_status_client_error() throws Exception {
        mockMvc.perform(get("/save-punishment"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_save_punishment_only_accept_json_if_not_then_return_status_client_error() throws Exception {
        PunishmentForm form = createPunishmentData("hello", "description");

        mockMvc.perform(post("/save-punishment").contentType(MediaType.TEXT_HTML)
                .content(convertObjectToJsonBytes(form)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_save_punishment_with_json_empty_content_then_return_status_client_error() throws Exception {

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_save_punishment_with_json_empty_fields_then_return_status_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData(null, null);

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // bad request
                .andExpect(status().is(400));
    }

    @Test
    public void post_to_save_punishment_only_accept_json_then_return_status_ok() throws Exception {
        PunishmentForm form = createPunishmentData("hello", "description");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                .andExpect(status().isOk());
    }

    @Test
    public void post_to_save_punishment_title_and_description_and_return_true() throws Exception {

        PunishmentForm form = createPunishmentData("hello", "description");

        mockMvc.perform(post("/save-punishment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void post_to_save_punishment_title_and_description_and_return_title_and_description() throws Exception {
        PunishmentForm form = createPunishmentData("hello", "description");

        mockMvc.perform(post("/save-punishment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.title", is("hello")));
    }

    @Test
    public void post_to_save_punishment_with_other_title_and_description_and_return_title_and_description() throws Exception {
        PunishmentForm form = createPunishmentData("pepe", "pepe");

        mockMvc.perform(post("/save-punishment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.description", is("pepe")))
                .andExpect(jsonPath("$.title", is("pepe")));
    }

    @Test
    public void post_to_save_punishment_return_id() throws Exception {
        PunishmentForm form = createPunishmentData("pepe", "pepe");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void post_to_save_punishment_then_return_id() throws Exception {
        PunishmentForm form = createPunishmentData("pepe", "pepe");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    // helpers

    private PunishmentForm createPunishmentData(String title, String description) {
        PunishmentForm form = new PunishmentForm();
        form.setTitle(title);
        form.setDescription(description);
        return form;
    }

    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }
}
