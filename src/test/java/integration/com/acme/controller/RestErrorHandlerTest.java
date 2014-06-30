package com.acme.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.service.AdService;
import com.acme.test.builder.AdBuilder;
import com.acme.test.builder.NewspaperBuilder;
import com.acme.test.util.TestUtil;

/**
 * We have to use an integration test to be able to ensure that the RestErrorHandler gets tested.
 * The @ControllerAdvice can unfortunately only be tested when using the WebApplicationContext and
 * not via the stand alone set up. This test class only contains the applicable error paths that
 * needs to be tested.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
@WebAppConfiguration
public class RestErrorHandlerTest {
  private MockMvc mockMvc;
  private MockRestServiceServer mockServer;
  private RestTemplate restTemplate;

  @Autowired
  private AdService adServiceMock;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setUp() {
    this.restTemplate = new RestTemplate();
    this.mockServer = MockRestServiceServer.createServer(restTemplate);
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void addAd_givenEmptyDescription_shouldReturnOneDescriptionValidationError()
      throws Exception {
    Ad added = AdBuilder.anAd().withAdTitle("my title").build();

    mockMvc
        .perform(
            post("/ads").contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(added))).andExpect(status().isBadRequest())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].code", is("description")));
    mockServer.verify();
  }

  @Test
  public void postAdToNewspaper_whenAdDoesNotExist_shouldReturnNotFound() throws Exception {
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    mockMvc
        .perform(
            post("/ads/999/newspapers/newspaper").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newspaper)))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].code", is(RestErrorHandler.POST_AD_ERROR)));
    mockServer.verify();
  }

  @Test
  public void retrieveAd_whenGivenNonNumericInput_shouldReturnBadRequest() throws Exception {
    String expectedErrorMessage =
        "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long';"
            + " nested exception is java.lang.NumberFormatException: For input string: \"bogus_data\"";
    mockMvc.perform(get("/ads/{ad}", "bogus_data")).andExpect(status().isBadRequest())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].code", is("Error")))
        .andExpect(jsonPath("$.errors[0].message", is(expectedErrorMessage)));

    mockServer.verify();
  }
}
