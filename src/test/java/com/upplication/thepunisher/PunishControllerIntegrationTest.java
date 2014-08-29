package com.upplication.thepunisher;

import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentForm.class})
public class PunishControllerIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Autowired PunishmentRepository punishmentRepository;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        // TODO: how to set transactional this method only and do: entityManager.createQuery("delete from Punishment").executeUpdate();
        punishmentRepository.deleteAll();
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
    public void post_to_save_punishment_then_return_id() throws Exception {
        PunishmentForm form = createPunishmentData("pepe", "pepe");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    @Test
    public void punishment_with_empty_title_then_return_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData("", "pepe");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void punishment_with_empty_description_then_return_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData("pepe", "");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void punishment_with_title_length_more_than_100_characters_then_return_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData("1234567890" +
                "1234567890" + "1234567890" + "1234567890" +"1234567890" +
                "1234567890" +"1234567890" + "1234567890" + "1234567890" +
                "1234567890" + "1"
                , "description");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isBadRequest());
    }

    @Test
    public void punishment_with_title_length_equal_than_100_characters_then_return_status_ok() throws Exception {
        PunishmentForm form = createPunishmentData("1234567890" +
                "1234567890" + "1234567890" + "1234567890" +"1234567890" +
                "1234567890" +"1234567890" + "1234567890" + "1234567890" +
                "1234567890"
                , "description");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk());
    }

    @Test
    public void punishment_with_description_length_more_than_100_characters_then_return_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData("title", "1234567890" +
                "1234567890" + "1234567890" + "1234567890" +"1234567890" +
                "1234567890" +"1234567890" + "1234567890" + "1234567890" +
                "1234567890" + "1");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isBadRequest());
    }
    @Test
    public void punishment_with_description_equal_than_100_characters_then_return_status_ok() throws Exception {
        PunishmentForm form = createPunishmentData("title", "1234567890" +
                "1234567890" + "1234567890" + "1234567890" +"1234567890" +
                "1234567890" +"1234567890" + "1234567890" + "1234567890" +
                "1234567890");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                // assert
                .andExpect(status().isOk());
    }

    @Test
    public void punishment_with_title_already_exists_then_return_bad_request() throws Exception {
        PunishmentForm form = createPunishmentData("title", "description");
        PunishmentForm form2 = createPunishmentData("title", "description");

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/save-punishment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(form2)))
                // assert
                .andExpect(status().isBadRequest());
    }


    // TODO: separate test and slim the configuration (mock backend)

    @Test
    public void get_create_punishment_then_return_html_with_form() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("<html>")))
                .andExpect(content().string(containsString("<body>")))
                .andExpect(content().string(containsString("</body>")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    public void get_create_punishment_then_return_html_with_input_title() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//input[@name=\"title\" and @type=\"text\"]").exists());
    }

    @Test
    public void get_create_punishment_then_return_html_with_input_description() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//input[@name=\"description\" and @type=\"text\"]").exists());
    }

    @Test
    public void get_create_punishment_then_return_html_with_submit_form() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//form/input[@type=\"submit\"]").exists());
    }

    @Test
    public void get_create_punishment_then_return_html_with_form_contains_input_title_and_description_and_submit() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//form[@action=\"save-punishment\"]").exists())
                .andExpect(xpath("//form/input[@type=\"submit\"]").exists())
                .andExpect(xpath("//form/input[@name=\"description\"]").exists())
                .andExpect(xpath("//form/input[@name=\"title\"]").exists());
    }

    // list punishment

    @Test
    public void get_list_punishment_only_accept_json_then_return_status_ok() throws Exception {

        mockMvc.perform(post("/list-punishment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void get_list_punishment_only_accept_json_then_return_list_punishment() throws Exception {

        punishmentRepository.create("pepe", "pepe");

        mockMvc.perform(post("/list-punishment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", is("pepe")))
                .andExpect(jsonPath("$[0].title", is("pepe")));
    }

    @Test
    public void get_another_list_punishment_only_accept_json_then_return_list_punishment() throws Exception {

        punishmentRepository.create("uea title", "pepe");
        punishmentRepository.create("another title", "pepe");

        mockMvc.perform(post("/list-punishment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", is("pepe")))
                .andExpect(jsonPath("$[0].title", is("another title")))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].description", is("pepe")))
                .andExpect(jsonPath("$[1].title", is("uea title")));
    }

    @Test
    public void get_create_punishment_return_the_previous_created_punishment_by_ajax() throws Exception {
        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id=\"list-punishments\"]").exists());
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
