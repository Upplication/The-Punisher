package com.upplication.thepunisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.upplication.config.PunishJpaTestConfig;
import com.upplication.config.PunishWebTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.DelegatingWebConnection;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.htmlunit.matchers.UrlRegexRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {PunishJpaTestConfig.class, PunishWebTestConfig.class,
        PunishmentRepository.class, PunishmentController.class,
        PunishmentForm.class})
public class PunishAdminIntegrationTest {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    protected WebClient webClient;


    @Autowired PunishmentRepository punishmentRepository;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
       // webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addRequestHeader("Accept-Language" , "es-ES");
       // new MockMvcHtmlUnitDriver(mockMvc, true)

        UrlRegexRequestMatcher cdnMatcher = new UrlRegexRequestMatcher("(.*?//code.jquery.com/.*)|" +
                "(.*?//cdnjs.cloudflare.com/.*)|" +
                "(.*?//maxcdn.bootstrapcdn.com/.*)");
        WebConnection httpConnection = new HttpWebConnection(webClient);
        WebConnection webConnection = new DelegatingWebConnection(new MockMvcWebConnection(mockMvc), new DelegatingWebConnection.DelegateWebConnection(cdnMatcher, httpConnection));
        webClient.setWebConnection(webConnection);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
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
                .andExpect(content().string(containsString("<html lang=\"en\">")))
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
                .andExpect(xpath("//form/button[@type=\"submit\"]").exists());
    }

    @Test
    public void get_create_punishment_then_return_html_with_form_contains_input_title_and_description_and_submit() throws Exception {

        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//form[@action=\"save-punishment\"]").exists())
                .andExpect(xpath("//form/button[@type=\"submit\"]").exists())
                .andExpect(xpath("//form//input[@name=\"description\"]").exists())
                .andExpect(xpath("//form//input[@name=\"title\"]").exists());
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
                .andExpect(xpath("//tbody[@id=\"list-punishments\"]").exists());
    }

    @Test
    public void get_create_punishment_return_link_to_roulette() throws Exception {
        mockMvc.perform(get("/create-punishment"))
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id=\"navigation\"]/ul/li/a").exists());
    }

    @Test
    public void create_punishment_add_first_to_the_list_of_punishments() throws IOException {
        // TODO: htmlunit claro que si
        // http://spring.io/blog/2014/03/25/spring-mvc-test-with-htmlunit
        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        submitPunishment(page, "Spring Rocks", "In case you didn't know, Spring Rocks!");

        // check

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        HtmlSpan spanTitle = tbody.getFirstByXPath("tr/td/span[@class=\"title\"]");
        HtmlSpan spanDesc = tbody.getFirstByXPath("tr/td/span[@class=\"description\"]");

        assertEquals(1, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals("Spring Rocks", spanTitle.getTextContent());
        assertEquals("In case you didn't know, Spring Rocks!", spanDesc.getTextContent());

    }

    private HtmlTextInput getDescription(HtmlPage page) {
        return page.getElementByName("description");
    }

    @Test
    public void create_punishment_page_get_all_list_of_punishments() throws Exception {
        // insert to the list
        punishmentRepository.create("title1", "desc1");
        punishmentRepository.create("title2", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlSpan> spanTitle = getTitles(tbody);
        List<HtmlSpan> spanDesc = getDescriptions(tbody);

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(2, spanTitle.size());
        assertEquals(2, spanDesc.size());
        assertEquals("title1", spanTitle.get(0).getTextContent());
        assertEquals("title2", spanTitle.get(1).getTextContent());
        assertEquals("desc1", spanDesc.get(0).getTextContent());
        assertEquals("desc2", spanDesc.get(1).getTextContent());
    }

    @Test
    public void create_punishment_page_get_another_all_list_of_punishments() throws Exception {
        // insert to the list
        punishmentRepository.create("bbbb", "desc1");
        punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlSpan> spanTitle = getTitles(tbody);
        List<HtmlSpan> spanDesc = getDescriptions(tbody);

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(2, spanTitle.size());
        assertEquals(2, spanDesc.size());
        assertEquals("aaaa", spanTitle.get(0).getTextContent());
        assertEquals("bbbb", spanTitle.get(1).getTextContent());
        assertEquals("desc2", spanDesc.get(0).getTextContent());
        assertEquals("desc1", spanDesc.get(1).getTextContent());
    }

    @Test
    public void create_punishment_with_empty_title_then_show_message_error() throws Exception {
        // insert to the list

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");
        submitPunishment(page, "", "In case you didn't know, Spring Rocks!");

        HtmlParagraph paragraph = page.getHtmlElementById("error");
        assertEquals("All fields are mandatory and title should be unique", paragraph.getTextContent());
        assertEquals("", paragraph.getAttribute("style"));
    }

    @Test
    public void create_punishment_with_empty_description_then_show_message_error() throws Exception {
        // insert to the list

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");
        submitPunishment(page, "Title", "");

        HtmlParagraph paragraph = page.getHtmlElementById("error");
        assertEquals("All fields are mandatory and title should be unique", paragraph.getTextContent());
        assertEquals("", paragraph.getAttribute("style"));
    }

    @Test
    public void create_punishment_page_get_all_list_of_punishments_with_delete_buttom() throws Exception {
        // insert to the list
        Punishment punishB = punishmentRepository.create("bbbb", "desc1");
        Punishment punishA = punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlTableDataCell> cellDelete = (List<HtmlTableDataCell>)tbody.getByXPath("tr/td[@class=\"delete\"]");

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(2, cellDelete.size());
        assertEquals(punishA.getId() + "", cellDelete.get(0).getAttribute("data-id"));
        assertEquals(punishB.getId() + "", cellDelete.get(1).getAttribute("data-id"));
    }

    @Test
    public void create_punishment_page_get_all_list_of_punishments_with_delete_buttom_with_text() throws Exception {
        // insert to the list
        punishmentRepository.create("bbbb", "desc1");
        punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlTableDataCell> cellDelete = (List<HtmlTableDataCell>)tbody.getByXPath("tr/td[@class=\"delete\"]");

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(2, cellDelete.size());
        assertEquals("Delete", ((HtmlButton)cellDelete.get(0).getByXPath("button").get(0)).getTextContent());
        assertEquals("Delete",  ((HtmlButton) cellDelete.get(1).getByXPath("button").get(0)).getTextContent());
    }

    @Test
    public void insert_punishment_then_print_the_punishment_with_delete_buttom() throws Exception {
        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        submitPunishment(page, "hola", "desc");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlTableDataCell> tdDelete = (List<HtmlTableDataCell>)tbody.getByXPath("tr/td[@class=\"delete\"]");

        assertEquals(1, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(1, tdDelete.size());
        assertNotNull(tdDelete.get(0).getAttribute("data-id"));
    }


    @Test
    public void delete_punishment_then_remove_from_list() throws Exception {
        // insert to the list
        punishmentRepository.create("bbbb", "desc1");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");
        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlButton> buttonDelete = (List<HtmlButton>)tbody.getByXPath("tr/td[@class=\"delete\"]/button");

        buttonDelete.get(0).click();

        assertEquals(0, tbody.getHtmlElementsByTagName("tr").size());
    }

    @Test
    public void delete_punishment_without_id_then_return_bad_request() throws Exception {

        mockMvc.perform(post("/delete-punishment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_punishment_with_exists_id_then_return_status_ok() throws Exception {
        // insert to the list
        Punishment punishment = punishmentRepository.create("bbbb", "desc1");

        PunishmentDeleteForm form = new PunishmentDeleteForm();
        form.setId(punishment.getId());

        mockMvc.perform(post("/delete-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_punishment_with_not_exists_id_then_return_status_bad_request() throws Exception {
        // insert to the list
        PunishmentDeleteForm form = new PunishmentDeleteForm();
        form.setId(1337);

        mockMvc.perform(post("/delete-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // edit

    @Test
    public void create_punishment_then_view_edit_option_aside_the_existing_punishments() throws Exception {
        punishmentRepository.create("bbbb", "desc1");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");
        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");

        List<HtmlButton> buttonEdit = getEditButtons(tbody);

        assertEquals(1, buttonEdit.size());
        assertNotNull(buttonEdit.get(0).getAttribute("data-id"));
    }

    @Test
    public void create_punishment_page_get_all_list_of_punishments_with_edit_buttom_with_text() throws Exception {
        // insert to the list
        punishmentRepository.create("bbbb", "desc1");
        punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlButton> buttonEdit = getEditButtons(tbody);

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(2, buttonEdit.size());
        assertEquals("Edit", buttonEdit.get(0).getTextContent());
        assertEquals("Edit", buttonEdit.get(1).getTextContent());
    }

    @Test
    public void edit_punishment_click_edit_then_change_title_and_description_text_by_input_text_and_the_edit_button_with_text_save() throws Exception {
        // insert to the list
        punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlButton> buttonEdit = getEditButtons(tbody);

        buttonEdit.get(0).click();

        HtmlInput titleInput = ((List<HtmlInput>)tbody.getByXPath("tr/td/input[@name=\"title\"]")).get(0);
        HtmlInput descriptionInput =  ((List<HtmlInput>)tbody.getByXPath("tr/td/input[@name=\"description\"]")).get(0);

        assertEquals("aaaa",titleInput.getValueAttribute());
        assertEquals("desc2",descriptionInput.getValueAttribute());
        assertEquals("Save", buttonEdit.get(0).getTextContent());
    }

    @Test
    public void edit_punishment_with_id_and_change_title_then_change_in_list_in_same_position() throws Exception {
        // insert to the list
        punishmentRepository.create("aaaa", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlButton> buttonEdit = getEditButtons(tbody);

        buttonEdit.get(0).click();

        HtmlTextInput titleInput = getEditedTitle(tbody).get(0);
        titleInput.setText("bubbubub");

        buttonEdit.get(0).click();

        List<HtmlSpan> spanTitle = getTitles(tbody);
        List<HtmlSpan> spanDesc = getDescriptions(tbody);

        assertEquals(1, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(1, spanTitle.size());
        assertEquals(1, spanDesc.size());
        assertEquals("bubbubub", spanTitle.get(0).getTextContent());
        assertEquals("desc2", spanDesc.get(0).getTextContent());
    }

    @Test
    public void edit_punishment_without_id_then_return_bad_request() throws Exception {

        mockMvc.perform(post("/edit-punishment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_punishment_with_exists_id_and_title_and_description_then_return_status_ok() throws Exception {
        // insert to the list
        Punishment punishment = punishmentRepository.create("bbbb", "desc1");

        PunishmentEditForm form = new PunishmentEditForm();
        form.setId(punishment.getId());
        form.setTitle("title");
        form.setDescription("descrtiption");

        mockMvc.perform(post("/edit-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void edit_punishment_with_exists_id_and_null_title_then_return_status_bad_request() throws Exception {
        // insert to the list
        Punishment punishment = punishmentRepository.create("bbbb", "desc1");

        PunishmentEditForm form = new PunishmentEditForm();
        form.setId(punishment.getId());
        form.setDescription("descrtiption");

        mockMvc.perform(post("/edit-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_punishment_with_exists_id_and_null_description_then_return_status_bad_request() throws Exception {
        // insert to the list
        Punishment punishment = punishmentRepository.create("bbbb", "desc1");

        PunishmentEditForm form = new PunishmentEditForm();
        form.setId(punishment.getId());
        form.setTitle("adads");

        mockMvc.perform(post("/edit-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_punishment_with_null_id_then_return_status_bad_request() throws Exception {
        PunishmentEditForm form = new PunishmentEditForm();
        form.setTitle("adads");
        form.setDescription("131dasdasdas");

        mockMvc.perform(post("/edit-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_punishment_with_not_exists_id_then_return_status_bad_request() throws Exception {
        // insert to the list
        PunishmentEditForm form = new PunishmentEditForm();
        form.setId(1337);
        form.setTitle("title");
        form.setDescription("descrtiption");

        mockMvc.perform(post("/edit-punishment")
                .content(convertObjectToJsonBytes(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_punishment_set_title_equal_another_then_show_message_error() throws Exception {
        // insert to the list
        punishmentRepository.create("aaaa", "desc2");
        punishmentRepository.create("bbbb", "desc2");

        HtmlPage page =
                webClient.getPage("http://localhost/the-punisher/create-punishment");

        HtmlTableBody tbody = page.getHtmlElementById("list-punishments");
        List<HtmlButton> buttonEdit = getEditButtons(tbody);
        // edit a.
        buttonEdit.get(0).click();

        HtmlTextInput titleInput = getEditedTitle(tbody).get(0);
        titleInput.setText("bbbb");

        buttonEdit.get(0).click();
        // NOTA: cuidado con el espacio entre display y none
        List<HtmlTableDataCell> tdError = (List<HtmlTableDataCell>)tbody.getByXPath("tr/td[@class=\"error\" and not(contains(@style,'display: none'))]");

        assertEquals(2, tbody.getHtmlElementsByTagName("tr").size());
        assertEquals(1, tdError.size());
        assertEquals("Title repeated", tdError.get(0).getTextContent());
    }

    // helpers


    private List<HtmlSpan> getDescriptions(HtmlTableBody div) {
        return (List<HtmlSpan>)div.getByXPath("tr/td/span[@class=\"description\"]");
    }

    private List<HtmlTextInput> getEditedTitle(HtmlTableBody div) {
        return (List<HtmlTextInput>)div.getByXPath("tr/td/input[@name=\"title\"]");
    }

    private List<HtmlSpan> getTitles(HtmlTableBody tbody) {
        return (List<HtmlSpan>)tbody.getByXPath("tr/td/span[@class=\"title\"]");
    }

    private List<HtmlButton> getEditButtons(HtmlTableBody tbody) {
        return (List<HtmlButton>)tbody.getByXPath("tr/td/button[contains(@class,'edit')]");
    }

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
    private void submitPunishment(HtmlPage page, String title, String description) throws IOException {
        HtmlTextInput titleInput = getTitle(page);
        titleInput.setValueAttribute(title);
        HtmlTextInput descriptionInput = getDescription(page);
        descriptionInput.setText(description);
        HtmlButton submit = getSubmit(page);
        submit.click();
    }

    private HtmlButton getSubmit(HtmlPage page) {
        return page.getHtmlElementById("save-punishment-form").getOneHtmlElementByAttribute("button", "type", "submit");
    }

    private HtmlTextInput getTitle(HtmlPage page) {
        return page.getElementByName("title");
    }
}
