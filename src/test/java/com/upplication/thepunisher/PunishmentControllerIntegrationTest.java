package com.upplication.thepunisher;

import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import com.upplication.thepunisher.requests.PunishmentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.inject.Inject;
import java.net.URLEncoder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentRequest.class})
public class PunishmentControllerIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

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
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", sampleString(101))
                .param("description", "description"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_with_invalid_description_length_should_fail() throws Exception {
        mockMvc.perform(post("/punishment/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "title")
                .param("description", sampleString(101)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void post_to_create_punishment_with_valid_data_should_create_punishment() throws Exception {
        mockMvc.perform(post("/punishment/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful());
    }

    private String sampleString(int n) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            s.append('c');
        }

        return s.toString();
    }

}