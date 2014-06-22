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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.repository.AdRepository;
import com.acme.test.builder.AdBuilder;
import com.acme.test.builder.NewspaperBuilder;

public class AdRepositoryServiceTest {

  private AdRepositoryService service;
  private AdRepository repositoryMock;

  @Before
  public void setUp() {
    repositoryMock = mock(AdRepository.class);
    service = new AdRepositoryService(repositoryMock);
  }

  @Test
  public void findSingleAd_whenGivenValidId_shouldReturnSingleAd() throws Exception {
    Ad setupAd = AdBuilder.anAd().withId(1).withAdTitle("foo").withAdDescription("bar").build();
    when(repositoryMock.findOne(1L)).thenReturn(setupAd);

    Ad returnedAd = service.findById(1L);

    verify(repositoryMock, times(1)).findOne(1L);
    verifyNoMoreInteractions(repositoryMock);

    assertThat(returnedAd, is(setupAd));
  }

  @Test
  public void findAll_whenTwoAdsExist_shouldReturnTwoAds() throws Exception {
    Ad firstSetupAd =
        AdBuilder.anAd().withId(1).withAdTitle("foo").withAdDescription("bar").build();
    Ad secondSetupAd =
        AdBuilder.anAd().withId(1).withAdTitle("foo").withAdDescription("bar").build();
    Collection<Ad> setupList = new HashSet<Ad>(Arrays.asList(firstSetupAd, secondSetupAd));
    when(repositoryMock.findAll()).thenReturn(setupList);

    Collection<Ad> returnedAds = service.findAll();

    verify(repositoryMock, times(1)).findAll();
    verifyNoMoreInteractions(repositoryMock);

    assertThat(returnedAds, is(setupList));

  }

  @Test
  public void addnewAd_whenValidAd_shouldReturnSavedAd() {
    Ad newAd = AdBuilder.anAd().withAdTitle("foo").withAdDescription("bar").build();

    service.save(newAd);

    ArgumentCaptor<Ad> adArgument = ArgumentCaptor.forClass(Ad.class);
    verify(repositoryMock, times(1)).save(adArgument.capture());
    verifyNoMoreInteractions(repositoryMock);

    Ad savedAd = adArgument.getValue();

    Assert.assertEquals(savedAd.getId(), newAd.getId());
    assertThat(savedAd.getTitle(), is(newAd.getTitle()));
    assertThat(savedAd.getDescription(), is(newAd.getDescription()));

  }

  @Test
  public void updateAd_whenValidInput_shouldReturnUpdatedAd() throws Exception {
    Ad existingAd = AdBuilder.anAd().withId(1).withAdTitle("foo").withAdDescription("bar").build();

    when(repositoryMock.save(any(Ad.class))).thenReturn(existingAd);

    service.save(existingAd);

    ArgumentCaptor<Ad> adArgument = ArgumentCaptor.forClass(Ad.class);
    verify(repositoryMock, times(1)).save(adArgument.capture());
    verifyNoMoreInteractions(repositoryMock);

    Ad savedAd = adArgument.getValue();

    assertEquals(savedAd.getId(), existingAd.getId());
    assertThat(savedAd.getTitle(), is(existingAd.getTitle()));
    assertThat(savedAd.getDescription(), is(existingAd.getDescription()));
  }

  @Test
  public void deleteAd_whenAdIsFound_shouldDeleteAndReturnAd() throws Exception {
    Ad existingAd = AdBuilder.anAd().withId(1).withAdTitle("foo").withAdDescription("bar").build();

    when(repositoryMock.findOne(1L)).thenReturn(existingAd);

    Ad adToDelete = service.deleteById(existingAd.getId());

    verify(repositoryMock, times(1)).findOne(1L);
    verify(repositoryMock, times(1)).delete(existingAd);
    verifyNoMoreInteractions(repositoryMock);

    assertThat(adToDelete, is(existingAd));
  }

  @Test
  public void postAdToNewspaper_whenFound_shouldReturnAd() throws Exception {
    Ad adToPost = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();

    when(repositoryMock.findOne(1L)).thenReturn(adToPost);

    service.postAdToNewspaper(1L, newspaper);

    verify(repositoryMock, times(1)).findOne(1L);
    verify(repositoryMock, times(1)).save(any(Ad.class));
    verifyNoMoreInteractions(repositoryMock);
  }

  @Test
  public void cancelAdToNewspaper_whenFound_shouldReturnAd() throws Exception {
    Ad adToCancel = AdBuilder.anAd().withId(1L).withAdTitle("foo").withAdDescription("bar").build();
    Newspaper newspaper =
        NewspaperBuilder.aNewspaper().withId(2L).withPublicationName("publication").build();

    when(repositoryMock.findOne(1L)).thenReturn(adToCancel);
    service.cancelAdInNewspaper(1L, newspaper);

    verify(repositoryMock, times(1)).findOne(1L);
    verify(repositoryMock, times(1)).save(any(Ad.class));
    verifyNoMoreInteractions(repositoryMock);
  }

}
