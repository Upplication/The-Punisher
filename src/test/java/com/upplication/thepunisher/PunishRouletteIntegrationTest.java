package com.upplication.thepunisher;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.DelegatingWebConnection;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.htmlunit.matchers.UrlRegexRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentForm.class})
public class PunishRouletteIntegrationTest {
    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    protected WebClient webClient;


    @Autowired
    PunishmentRepository punishmentRepository;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
        // webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("Accept-Language" , "es-ES");
        // new MockMvcHtmlUnitDriver(mockMvc, true)

        UrlRegexRequestMatcher cdnMatcher = new UrlRegexRequestMatcher("(.*?//code.jquery.com/.*)|(.*?//cdnjs.cloudflare.com/.*)");
        WebConnection httpConnection = new HttpWebConnection(webClient);
        WebConnection webConnection = new DelegatingWebConnection(new MockMvcWebConnection(mockMvc), new DelegatingWebConnection.DelegateWebConnection(cdnMatcher, httpConnection));
        webClient.setWebConnection(webConnection);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        // TODO: how to set transactional this method only and do: entityManager.createQuery("delete from Punishment").executeUpdate();
        punishmentRepository.deleteAll();
    }

    @Test
    public void list_roulette_then_show_punishments_inside_div() throws Exception {

        punishmentRepository.create("aaaa", "desc2");
        punishmentRepository.create("bbbb", "desc2");

        mockMvc.perform(get("/roulette-punishments"))
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id=\"roulette\"]").exists())
                .andExpect(xpath("//div[@id=\"roulette\"]/span[@class=\"punish\"]").exists());
    }

    @Test
    public void list_roulette_without_pusnihsment_then_show_button_go_to_admin_page() throws Exception {
        mockMvc.perform(get("/roulette-punishments"))
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id=\"roulette\"]").exists())
                .andExpect(xpath("//div[@id=\"roulette\"]/a").exists());
    }

    @Test
    public void list_roulette_then_you_can_spin_the_roulette() throws Exception {
        // insert to the list
        punishmentRepository.create("aaaa", "desc2");
        punishmentRepository.create("bbbb", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/roulette-punishments");

        HtmlDivision div = page.getHtmlElementById("roulette");
        // TODO: click to spin
    }
}
