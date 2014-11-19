package com.upplication.thepunisher;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import org.junit.After;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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
        webClient.addRequestHeader("Accept-Language", "es-ES");
        // new MockMvcHtmlUnitDriver(mockMvc, true)

        UrlRegexRequestMatcher cdnMatcher = new UrlRegexRequestMatcher("(.*?//code.jquery.com/.*)|(.*?//d3lp1msu2r81bx.cloudfront.net/.*)|(.*?//cdnjs.cloudflare.com/ajax/libs/mustache.js/.*)");
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
                .andExpect(xpath("//div[@id=\"roulette\"]/canvas").exists());
    }

    @Test
    public void list_roulette_without_punishment_then_show_button_go_to_admin_page() throws Exception {
        mockMvc.perform(get("/roulette-punishments"))
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id=\"navigation\"]").exists())
                .andExpect(xpath("//div[@id=\"navigation\"]/a").exists())
                .andExpect(xpath("//div[@id=\"roulette\"]").doesNotExist());
    }

    @Test
    public void list_roulette_then_you_get_punishmemtns_in_vertical() throws Exception {
        // insert to the list
        punishmentRepository.create("abc", "desc2");
        punishmentRepository.create("def", "desc2");

        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/roulette-punishments");

        assertThat(page.getHead().asXml(), containsString("ELEMENTS = ['a\\nb\\nc\\n','d\\ne\\nf\\n']"));
    }

    @Test
    public void list_roulette_then_you_save_in_javascript_all_data() throws Exception {
        // insert to the list
        punishmentRepository.create("abc", "desc2");
        punishmentRepository.create("def", "desc2");

        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/roulette-punishments");

        assertThat(page.getHead().asXml(), containsString("ALL_DATA = [{'description':'desc2','title':'abc','titleRoulette':'a\\nb\\nc\\n'}," +
                "{'description':'desc2','title':'def','titleRoulette':'d\\ne\\nf\\n'}]"));
    }

    @Test
    public void list_roulette_without_punish_then_dont_render_javascript() throws Exception {
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/roulette-punishments");

        assertThat(page.getHead().asXml(), not(containsString("var ELEMENTS")));
    }

    //@Test
    public void list_roulette_then_you_can_spin_and_get_the_selected() throws Exception {
        // insert to the list
        punishmentRepository.create("abc", "desc2");
        punishmentRepository.create("def", "desc2");

        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/roulette-punishments");

        HtmlElement div = page.getHtmlElementById("roulette");
        div.mouseDown();
        div.mouseMove();
        div.mouseUp();

        webClient.waitForBackgroundJavaScript(1000);

        HtmlElement element = page.getHtmlElementById("result");

        assertNotNull(element);
    }
}
