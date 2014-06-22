package com.acme.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.acme.dom.Newspaper;
import com.acme.repository.NewspaperRepository;
import com.acme.test.builder.NewspaperBuilder;

public class NewspaperRepositoryServiceTest {

  private NewspaperRepositoryService service;
  private NewspaperRepository repositoryMock;

  @Before
  public void setUp() {
    repositoryMock = mock(NewspaperRepository.class);
    service = new NewspaperRepositoryService(repositoryMock);
  }

  @Test
  public void findSingleNewspaper_whenGivenValidId_shouldReturnSingleNewspaper() throws Exception {
    Newspaper setupNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();
    when(repositoryMock.findOne(1L)).thenReturn(setupNewspaper);

    Newspaper returnedNewspaper = service.findById(1L);

    verify(repositoryMock, times(1)).findOne(1L);
    verifyNoMoreInteractions(repositoryMock);

    assertThat(returnedNewspaper, is(setupNewspaper));
  }

  @Test
  public void findAll_whenTwoNewspaperExist_shouldReturnBoth() throws Exception {
    Newspaper firstNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("first").build();
    Newspaper secondNewspaper =
        NewspaperBuilder.aNewspaper().withId(2).withPublicationName("second").build();

    Collection<Newspaper> setupList =
        new HashSet<Newspaper>(Arrays.asList(firstNewspaper, secondNewspaper));
    when(repositoryMock.findAll()).thenReturn(setupList);

    Collection<Newspaper> returnedNewspapers = service.findAll();

    verify(repositoryMock, times(1)).findAll();
    verifyNoMoreInteractions(repositoryMock);

    assertThat(returnedNewspapers, is(setupList));
  }

  @Test
  public void addnewNewspaper_whenValid_shouldReturnSavedNewspaper() {
    Newspaper newNewspaper =
        NewspaperBuilder.aNewspaper().withPublicationName("New York Times").build();

    service.save(newNewspaper);

    ArgumentCaptor<Newspaper> newspaperArgument = ArgumentCaptor.forClass(Newspaper.class);
    verify(repositoryMock, times(1)).save(newspaperArgument.capture());
    verifyNoMoreInteractions(repositoryMock);

    Newspaper savedNewspaper = newspaperArgument.getValue();

    assertEquals(savedNewspaper.getId(), newNewspaper.getId());
    assertThat(savedNewspaper.getPublicationName(), is(newNewspaper.getPublicationName()));
  }

  @Test
  public void updateNewspaper_whenValidInput_shouldReturnUpdatedNewspaper() throws Exception {
    Newspaper existingNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    when(repositoryMock.save(any(Newspaper.class))).thenReturn(existingNewspaper);

    service.save(existingNewspaper);

    ArgumentCaptor<Newspaper> newspaperArgument = ArgumentCaptor.forClass(Newspaper.class);
    verify(repositoryMock, times(1)).save(newspaperArgument.capture());
    verifyNoMoreInteractions(repositoryMock);

    Newspaper savedNewspaper = newspaperArgument.getValue();

    assertEquals(savedNewspaper.getId(), existingNewspaper.getId());
    assertThat(savedNewspaper.getPublicationName(), is(existingNewspaper.getPublicationName()));
  }

  @Test
  public void deleteNewspaper_whenNewspaperIsFound_shouldDeleteAndReturnNewspaper()
      throws Exception {
    Newspaper existingNewspaper =
        NewspaperBuilder.aNewspaper().withId(1).withPublicationName("New York Times").build();

    when(repositoryMock.findOne(1L)).thenReturn(existingNewspaper);

    Newspaper newspaperToDelete = service.deleteById(existingNewspaper.getId());

    verify(repositoryMock, times(1)).findOne(1L);
    verify(repositoryMock, times(1)).delete(existingNewspaper);
    verifyNoMoreInteractions(repositoryMock);

    assertThat(newspaperToDelete, is(existingNewspaper));
  }

}
