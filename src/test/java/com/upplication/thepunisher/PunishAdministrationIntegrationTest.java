package com.upplication.thepunisher;

import com.upplication.config.WebSecurityConfigurationAware;
import org.junit.Assert;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class PunishAdministrationIntegrationTest extends WebSecurityConfigurationAware {

  /*
    @Test
    public void when_access_to_punsih_url_then_return_input_title_and_input_description_with_a_button_to_create_the_punishment(){
        mockMvc.perform(get("/admin/punish"))
                .andExpect(redirectedUrl("http://localhost/signin"));
    }*/
}
