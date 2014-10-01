package com.upplication.thepunisher;

import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class})
public class PunishmentControllerIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        // TODO: how to set transactional this method only and do: entityManager.createQuery("delete from Punishment").executeUpdate();
    }

    @Test
    public void get_to_url_punish_slash_create_then_return_html() throws Exception {
        mockMvc.perform(get("/punish/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("<html>")))
                .andExpect(content().string(containsString("<body>")))
                .andExpect(content().string(containsString("</body>")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    public void there_is_a_title_field_in_create_page() throws Exception {
        mockMvc.perform(get("/punish/create"))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/form/input[@type=\"title\" and @name=\"title\"]").exists());
    }

    @Test
    public void there_is_a_description_field_in_create_page() throws Exception {
        mockMvc.perform(get("/punish/create"))
                .andExpect(xpath("/html/body/form/input[@type=\"description\" and @name=\"description\"]").exists());
    }

    @Test
    public void there_is_a_form_with_description_field_and_title_field_in_create_page() throws Exception {
        mockMvc.perform(get("/punish/create"))
                .andExpect(xpath("/html/body/form/input[@type=\"title\" and @name=\"title\"]").exists())
                .andExpect(xpath("/html/body/form/input[@type=\"description\" and @name=\"description\"]").exists());
    }

    @Test
    public void there_is_a_form_with_description_field_and_title_field_and_submit_buttom_in_create_page() throws Exception {
        mockMvc.perform(get("/punish/create"))
                .andExpect(xpath("/html/body/form/input[@type=\"title\" and @name=\"title\"]").exists())
                .andExpect(xpath("/html/body/form/input[@type=\"description\" and @name=\"description\"]").exists())
                .andExpect(xpath("/html/body/form/input[@type=\"submit\" and @value=\"submit\"]").exists());
    }
}
