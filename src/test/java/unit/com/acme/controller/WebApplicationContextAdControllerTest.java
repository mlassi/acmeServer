package com.acme.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.acme.dom.Ad;
import com.acme.service.AdService;
import com.acme.test.util.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@WebAppConfiguration
public class WebApplicationContextAdControllerTest {

	private MockMvc mockMvc;
	private MockRestServiceServer mockServer;
	private RestTemplate restTemplate;
	
    @Autowired
    private AdService adServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        //Mockito.reset(adServiceMock);

    	this.restTemplate = new RestTemplate();
        //mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    	this.mockServer = MockRestServiceServer.createServer(restTemplate);
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    
    }
    
    
    @Test
    public void add_EmptyTodoEntry_ShouldRenderFormViewAndReturnValidationErrorForTitle() throws Exception {
    	Ad added = new Ad();
    	
        mockMvc.perform(post("/ads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(added))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0].code", is("title")))
                .andExpect(jsonPath("$.errors[0].message", is("The title cannot be empty.")))
        .andExpect(jsonPath("$.errors[1].code", is("description")))
        .andExpect(jsonPath("$.errors[1].message", is("The description cannot be empty.")));
        mockServer.verify();
       // verifyZeroInteractions(adServiceMock);
    	
        

    }
}
