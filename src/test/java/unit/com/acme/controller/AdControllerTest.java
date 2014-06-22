package com.acme.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.acme.test.builder.AdBuilder;
import com.acme.test.builder.NewspaperBuilder;
import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.test.util.TestUtil;
import com.acme.service.AdService;

@Configuration
@WebAppConfiguration
public class AdControllerTest {

  @Mock
  private AdService adServiceMock;

  @InjectMocks
  private AdController adController;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
  }

  @Test
  public void retrieveSingleAd_whenFound_ShouldReturnFoundSingleAd() throws Exception {
    Ad singleAd = AdBuilder.anAd().withId(1L).build();
    singleAd.setTitle("foo");
    singleAd.setDescription("Lorem ipsum");
    when(adServiceMock.findById(1)).thenReturn(singleAd);

    mockMvc.perform(get("/ads/{ad}", 1L)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.description", is("Lorem ipsum")));

  }

  @Test
  public void retrieveSingleAd_whenNotFound_ShouldReturnHttpStatusCode404() throws Exception {
    when(adServiceMock.findById(99L)).thenReturn(null);

    mockMvc.perform(get("/ads/{id}", 99L)).andExpect(status().isNotFound());

    verify(adServiceMock, times(1)).findById(99L);
    verifyNoMoreInteractions(adServiceMock);
  }

  @Test
  public void retrieveAllAds_whenFound_shouldReturnTwoAds() throws Exception {
    Ad first = AdBuilder.anAd().withId(1L).withAdTitle("first ad").withAdDescription("uno").build();
    Ad second =
        AdBuilder.anAd().withId(2L).withAdTitle("second ad").withAdDescription("duo").build();

    when(adServiceMock.findAll()).thenReturn(Arrays.asList(first, second));

    mockMvc.perform(get("/ads")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].description", is("uno")))
        .andExpect(jsonPath("$[0].title", is("first ad"))).andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].description", is("duo")))
        .andExpect(jsonPath("$[1].title", is("second ad")));

    verify(adServiceMock, times(1)).findAll();
    verifyNoMoreInteractions(adServiceMock);
  }

  @Test
  public void retrieveAllAds_whenNoneFound_shouldReturnEmptyArray() throws Exception {

    when(adServiceMock.findAll()).thenReturn(null);

    mockMvc.perform(get("/ads")).andExpect(status().isOk());

    verify(adServiceMock, times(1)).findAll();
    verifyNoMoreInteractions(adServiceMock);
  }

  @Test
  public void addAd_whenAdHasValidFormat_shouldReturnTheAd() throws Exception {
    Ad added = AdBuilder.anAd().withAdTitle("foo").withAdDescription("bar").build();

    when(adServiceMock.save(added)).thenReturn(added);

    mockMvc
        .perform(
            post("/ads").contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(added))).andExpect(status().isCreated())
        .andReturn();

    ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
    verify(adServiceMock, times(1)).save(adCaptor.capture());
    verifyNoMoreInteractions(adServiceMock);

    Ad adArgument = adCaptor.getValue();
    assertThat(adArgument.getDescription(), is("bar"));
    assertThat(adArgument.getTitle(), is("foo"));
  }

  @Test
  public void addAd_EmptyAd_ShouldReturnInvalidRequest() throws Exception {
    Ad ad = new Ad();

    mockMvc.perform(
        post("/ads").contentType(TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(ad))).andExpect(status().isBadRequest());

    verifyZeroInteractions(adServiceMock);
  }

  @Test
  public void updateAd_whenAdHasValidFormat_shouldReturnTheAd() throws Exception {
    Ad added = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();

    when(adServiceMock.save(any(Ad.class))).thenReturn(added);

    mockMvc
        .perform(
            put("/ads/{id}", 1L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(added))).andExpect(status().isOk()).andReturn();

    ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
    verify(adServiceMock, times(1)).save(adCaptor.capture());
    verifyNoMoreInteractions(adServiceMock);

    Ad adArgument = adCaptor.getValue();
    assertThat(adArgument.getDescription(), is("bar"));
    assertThat(adArgument.getTitle(), is("foo"));
  }

  @Test
  public void updatedAd_EmptyAd_ShouldReturnInvalidRequest() throws Exception {
    Ad ad = AdBuilder.anAd().withId(1).build();

    mockMvc.perform(
        put("/ads/{id}", 1L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(ad))).andExpect(status().isBadRequest());

    verifyZeroInteractions(adServiceMock);
  }

  @Test
  public void updatedAd_whenGivenIdThatCantBeFound_ReturnHttpStatus404() throws Exception {
    Ad added = AdBuilder.anAd().withId(99L).withAdTitle("foo").withAdDescription("bar").build();

    when(adServiceMock.save(added)).thenReturn(null);

    mockMvc
        .perform(
            put("/ads/{id}", 99L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(added))).andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  public void deleteAd_whenGivenExistingId_shouldReturnHttpStatusCode200() throws Exception {
    Ad deletedAd = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();
    when(adServiceMock.deleteById(1L)).thenReturn(deletedAd);

    mockMvc.perform(delete("/ads/{id}", 1L)).andExpect(status().isOk()).andReturn();

    verify(adServiceMock, times(1)).deleteById(1L);
    verifyNoMoreInteractions(adServiceMock);
  }

  @Test
  public void deleteAd_whenNotFound_shouldReturnHttpStatusCode404() throws Exception {
    when(adServiceMock.deleteById(1L)).thenReturn(null);

    mockMvc.perform(delete("/ads/{id}", 1L)).andExpect(status().isNotFound()).andReturn();

    verify(adServiceMock, times(1)).deleteById(1L);
    verifyNoMoreInteractions(adServiceMock);
  }

  @Test
  public void postAdToNewspaper_whenGivenValidAdAndNewspaper_shouldReturnHttpStatusCode201()
      throws Exception {
    Ad ad = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();
   
    when(adServiceMock.postAdToNewspaper(eq(1L), any(Newspaper.class))).thenReturn(ad);

    mockMvc
        .perform(
            post("/ads/{id}/newspapers/{newspaper}", ad.getId(), newspaper).contentType(
                TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(newspaper))).andExpect(status().isCreated())
        .andReturn();

  }

  @Test
  public void postAdToNewspaper_whenGivenInvalidAdOrNewspaper_shouldReturnHttpStatusCode404()
      throws Exception {
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();
    when(adServiceMock.postAdToNewspaper(99L, newspaper)).thenReturn(null);

    mockMvc
        .perform(
            post("/ads/{id}/newspapers/{newspaper}", 99L, newspaper).contentType(
                TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(newspaper))).andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  public void cancelAdInNewspaper_whenGivenValidAdAndNewspaper_shouldREturnHttpStatusCode200()
      throws Exception {

    Ad ad = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();

    when(adServiceMock.cancelAdInNewspaper(eq(1L), any(Newspaper.class))).thenReturn(ad);

    mockMvc
        .perform(
            delete("/ads/{id}/newspapers/{newspaper}", ad.getId(), newspaper).contentType(
                TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(newspaper))).andExpect(status().isOk())
        .andReturn();

  }

  @Test
  public void cancelAdInNewspaper_whenGivenInvalidAdOrNewspaper_shouldReturnHttpStatusCode404()
      throws Exception {
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();
    when(adServiceMock.postAdToNewspaper(99L, newspaper)).thenReturn(null);

    mockMvc
        .perform(
            delete("/ads/{id}/newspapers/{newspaper}", 99L, newspaper).contentType(
                TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(newspaper))).andExpect(status().isNotFound())
        .andReturn();
  }



}
