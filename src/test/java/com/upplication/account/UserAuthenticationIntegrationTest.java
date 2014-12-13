package com.upplication.account;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import com.upplication.config.WebSecurityConfigurationAware;

public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {

    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void initialize(){
        if (accountRepository.findByEmail("user") == null){
            accountRepository.save(new Account("user", "demo", "ROLE_USER"));
            accountRepository.save(new Account("admin", "admin", "ROLE_ADMIN"));
        }
    }
    //@Test
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/account/current"))
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    public void userAuthenticates() throws Exception {
        final String username = "user";
        ResultMatcher matcher = new ResultMatcher() {
            public void match(MvcResult mvcResult) throws Exception {
                HttpSession session = mvcResult.getRequest().getSession();
                SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                Assert.assertEquals(securityContext.getAuthentication().getName(), username);
            }
        };
        mockMvc.perform(post("/j_spring_security_check").param("j_username", username).param("j_password", "demo"))
                .andExpect(redirectedUrl("/"))
                .andExpect(matcher);
    }

    @Test
    public void userAuthenticationFails() throws Exception {
        final String username = "user";
        mockMvc.perform(post("/j_spring_security_check").param("j_username", username).param("j_password", "invalid"))
                .andExpect(redirectedUrl("/signin?error=1"))
                .andExpect(new ResultMatcher() {
                    public void match(MvcResult mvcResult) throws Exception {
                        HttpSession session = mvcResult.getRequest().getSession();
                        SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                        Assert.assertNull(securityContext);
                    }
                });
    }
}
