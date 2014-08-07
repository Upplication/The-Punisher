package com.upplication.thepunisher;

import com.upplication.config.PunishTestConfig;
import com.upplication.config.WebMvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.nio.charset.Charset;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishTestConfig.class, PunishmentRepository.class, PunishmentController.class})
public class PunishControllerIT {

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
        mockMvc.perform(post("/save-punishment").contentType(MediaType.TEXT_HTML))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_save_punishment_only_accept_json_then_return_status_ok() throws Exception {
        mockMvc.perform(post("/save-punishment").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void post_to_save_punishment_title_and_description_and_return_true() throws Exception {
        mockMvc.perform(post("/save-punishment")

                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{title: \"hola\", description: \"description\"}")
                    .accept(MediaType.APPLICATION_JSON))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void post_to_save_punishment_title_and_description_and_return_title_and_description() throws Exception {
        mockMvc.perform(post("/save-punishment")

                .contentType(MediaType.APPLICATION_JSON)
                .content("{title: \"hola\", description: \"description\"}")
                .accept(MediaType.APPLICATION_JSON))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.title", is("hola")));
    }

    @Test
    public void post_to_save_punishment_with_othre_title_and_description_and_return_title_and_description() throws Exception {
        mockMvc.perform(post("/save-punishment")

                .contentType(MediaType.APPLICATION_JSON)
                .content("{title: \"pepe\", description: \"pepe\"}")
                .accept(MediaType.APPLICATION_JSON))
                // assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.description", is("pepe")))
                .andExpect(jsonPath("$.title", is("pepe")));
    }
        /* TODO
    @Test
    public void post_to_save_punishment_without_title_and_description_and_return_false() throws Exception {
        mockMvc.perform(post("/save-punishment").
                contentType(MediaType.APPLICATION_JSON).content("{title: \"hola\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }
    */
}
