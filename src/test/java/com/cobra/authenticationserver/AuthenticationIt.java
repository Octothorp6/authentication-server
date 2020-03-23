package com.cobra.authenticationserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthenticationIt {
    @Autowired
    private MockMvc mockMvc;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetToken() throws Exception {
        // Invoke the token url and receive a token in return
        MvcResult result = getResult();

        String tokenString = result.getResponse().getContentAsString();

        Map<String, Object> map = mapper.readValue(tokenString, new TypeReference<Map<String, Object>>() {});
        assertTrue(map.containsKey("access_token"));
    }

    @Test
    public void testCheckToken() throws Exception {
        // Invoke the token url and receive a token in return
        MvcResult result = getResult();

        String tokenString = result.getResponse().getContentAsString();
        // Map the response
        Map<String, Object> map = mapper.readValue(tokenString, new TypeReference<Map<String, Object>>() {});

        result = this.mockMvc.perform(post("/oauth/check_token")
                .with(httpBasic(clientId,clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("token=" +map.get("access_token")))
                .andExpect(status().isOk()).andReturn();
        tokenString = result.getResponse().getContentAsString();
        map = mapper.readValue(tokenString, new TypeReference<Map<String, Object>>() {});
        assertTrue(map.containsKey("user_name"));
    }

    @Test
    public void testCheckMe() throws Exception {
        // Invoke the token url and receive a token in return
        MvcResult result = getResult();

        String tokenString = result.getResponse().getContentAsString();
        // Map the response
        Map<String, Object> map = mapper.readValue(tokenString, new TypeReference<Map<String, Object>>() {});

        result = this.mockMvc.perform(get("/user/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization","Bearer "+map.get("access_token")))
                .andExpect(status().isOk())
                .andReturn();
        tokenString = result.getResponse().getContentAsString();
        map = mapper.readValue(tokenString,new TypeReference<Map<String,Object>>() {});
        assertTrue(map.containsKey("username"));
    }

    @Test
    public void testEncrypt() throws Exception {
        // Invoke the token url and receive a token in return
        MvcResult result = getResult();

        String tokenString = result.getResponse().getContentAsString();
        // Map the response
        Map<String, Object> map = mapper.readValue(tokenString, new TypeReference<Map<String, Object>>() {});

        result = this.mockMvc.perform(post("/encrypt")
                .header("Authorization","Bearer "+map.get("access_token"))
                .contentType(MediaType.TEXT_PLAIN)
                .content("test"))
                .andExpect(status().isOk()).andReturn();
        String encryptedPassword = result.getResponse().getContentAsString();
        System.out.println(encryptedPassword);
        assertNotNull(encryptedPassword);
    }

    // Helper method to Invoke the token url and receive a token in return
    private MvcResult getResult() throws Exception {
        return this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId,clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("grant_type=password&username=tester&password=123456"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
