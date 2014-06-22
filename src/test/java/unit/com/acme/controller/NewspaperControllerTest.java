package com.acme.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.acme.dom.Newspaper;
import com.acme.service.NewspaperService;
import com.acme.test.builder.NewspaperBuilder;
import com.acme.test.util.TestUtil;

@Configuration
@WebAppConfiguration
public class NewspaperControllerTest {

  @Mock
  private NewspaperService newspaperServiceMock;

  @InjectMocks
  private NewspaperController newspaperController;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(newspaperController).build();
  }

  @Test
  public void retrieveSingleNewspaper_whenFound_ShouldReturnFoundSingleNewspaper() throws Exception {
    Newspaper singleNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();
    when(newspaperServiceMock.findById(1)).thenReturn(singleNewspaper);

    mockMvc.perform(get("/newspapers/{id}", 1L)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.publicationName", is("New York Times")));
  }

  @Test
  public void retrieveSingleNewspaper_whenNotFound_ShouldReturnHttpStatusCode404() throws Exception {
    when(newspaperServiceMock.findById(99L)).thenReturn(null);

    mockMvc.perform(get("/newspapers/{id}", 99L)).andExpect(status().isNotFound());

    verify(newspaperServiceMock, times(1)).findById(99L);
    verifyNoMoreInteractions(newspaperServiceMock);
  }

  @Test
  public void retrieveAllNewspapers_whenFound_shouldReturnTwoNewspapers() throws Exception {
    Newspaper first =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("first newspaper").build();
    Newspaper second =
        NewspaperBuilder.aNewspaper().withId(2).withPublicationName("second newspaper").build();

    when(newspaperServiceMock.findAll()).thenReturn(Arrays.asList(first, second));

    mockMvc.perform(get("/newspapers")).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].publicationName", is("first newspaper")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].publicationName", is("second newspaper")));

    verify(newspaperServiceMock, times(1)).findAll();
    verifyNoMoreInteractions(newspaperServiceMock);
  }

  @Test
  public void retrieveAllNewspaper_whenNoneFound_shouldReturnEmptyArray() throws Exception {
    when(newspaperServiceMock.findAll()).thenReturn(null);

    mockMvc.perform(get("/newspapers")).andExpect(status().isOk());

    verify(newspaperServiceMock, times(1)).findAll();
    verifyNoMoreInteractions(newspaperServiceMock);
  }

  @Test
  public void addNewspaper_whenNewspaperHasValidFormat_shouldReturnTheNewspaper() throws Exception {
    Newspaper addingNewspaper =
        NewspaperBuilder.aNewspaper().withPublicationName("New York Times").build();

    when(newspaperServiceMock.save(addingNewspaper)).thenReturn(addingNewspaper);

    mockMvc
        .perform(
            post("/newspapers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(addingNewspaper)))
        .andExpect(status().isCreated())
        .andReturn();

    ArgumentCaptor<Newspaper> newspaperCaptor = ArgumentCaptor.forClass(Newspaper.class);
    verify(newspaperServiceMock, times(1)).save(newspaperCaptor.capture());
    verifyNoMoreInteractions(newspaperServiceMock);

    Newspaper newspaperArgument = newspaperCaptor.getValue();
    assertThat(newspaperArgument.getPublicationName(), is("New York Times"));
  }

  @Test
  public void addNewspaper_whenNewspaperIsEmpty_ShouldReturnInvalidRequest() throws Exception {
    Newspaper addingNewspaper = new Newspaper();

    mockMvc.perform(
        post("/newspapers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(addingNewspaper))).andExpect(status().isBadRequest());

    verifyZeroInteractions(newspaperServiceMock);
  }

  @Test
  public void updateNewspaper_whenNewspaperHasValidFormat_shouldReturnTheNewspaper()
      throws Exception {
    Newspaper addingNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    when(newspaperServiceMock.save(any(Newspaper.class))).thenReturn(addingNewspaper);

    mockMvc
        .perform(
            put("/newspapers/{id}", 1L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(addingNewspaper))).andExpect(status().isOk())
        .andReturn();

    ArgumentCaptor<Newspaper> newspaperCaptor = ArgumentCaptor.forClass(Newspaper.class);
    verify(newspaperServiceMock, times(1)).save(newspaperCaptor.capture());
    verifyNoMoreInteractions(newspaperServiceMock);

    Newspaper newspaperArgument = newspaperCaptor.getValue();
    assertThat(newspaperArgument.getPublicationName(), is("New York Times"));
  }

  @Test
  public void updateNewspaper_whenNewspaperBodyIsEmpty_shouldReturnInvalidRequest()
      throws Exception {
    Newspaper updatingNewspaper = NewspaperBuilder.aNewspaper().withId(1).build();

    mockMvc.perform(
        put("/newspapers/{id}", 1L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(updatingNewspaper))).andExpect(
        status().isBadRequest());

    verifyZeroInteractions(newspaperServiceMock);
  }

  @Ignore
  @Test
  public void updateNewspaper_whenNewspaperIsValidButExceptionOccurs_shouldReturnHttpStatus500()
      throws Exception {
    Newspaper updatingNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    when(newspaperServiceMock.save(updatingNewspaper)).thenThrow(
        new IllegalArgumentException("stuff happens"));

    mockMvc
        .perform(
            put("/newspapers/{id}", 1L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(updatingNewspaper)))
        .andExpect(status().isInternalServerError()).andReturn();

    ArgumentCaptor<Newspaper> newspaperCaptor = ArgumentCaptor.forClass(Newspaper.class);
    verify(newspaperServiceMock, times(1)).save(newspaperCaptor.capture());
    verifyNoMoreInteractions(newspaperServiceMock);

    Newspaper newspaperArgument = newspaperCaptor.getValue();
    assertThat(newspaperArgument.getPublicationName(), is("New York Times"));
  }

  @Test
  public void updateNewspaper_whenGivenIdThatCantBeFound_ReturnHttpStatus404() throws Exception {
    Newspaper updatingNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    when(newspaperServiceMock.save(updatingNewspaper)).thenReturn(null);

    mockMvc
        .perform(
            put("/newspapers/{id}", 99L).contentType(TestUtil.APPLICATION_JSON_UTF8).content(
                TestUtil.convertObjectToJsonBytes(updatingNewspaper)))
        .andExpect(status().isNotFound()).andReturn();

  }

  @Test
  public void deleteNewspaper_whenGivenExistingId_shouldReturnHttpStatusCode200() throws Exception {
    Newspaper deletingNewspaper = NewspaperBuilder.aNewspaper().withId(1).build();
    when(newspaperServiceMock.deleteById(1L)).thenReturn(deletingNewspaper);

    mockMvc.perform(delete("/newspapers/{id}", 1L)).andExpect(status().isOk()).andReturn();

    verify(newspaperServiceMock, times(1)).deleteById(1L);
    verifyNoMoreInteractions(newspaperServiceMock);
  }

  @Test
  public void deleteNewspaper_whenNotFound_shouldReturnHttpStatusCode404() throws Exception {
    when(newspaperServiceMock.deleteById(1L)).thenReturn(null);

    mockMvc.perform(delete("/newspapers/{id}", 1L)).andExpect(status().isNotFound()).andReturn();

    verify(newspaperServiceMock, times(1)).deleteById(1L);
    verifyNoMoreInteractions(newspaperServiceMock);
  }

}
