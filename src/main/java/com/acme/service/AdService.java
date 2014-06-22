package com.acme.service;

import java.util.Collection;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;

public interface AdService {
  public Ad findById(long id);

  public Collection<Ad> findAll();

  public Ad save(Ad ad);

  public Ad deleteById(long id);

  public Ad postAdToNewspaper(long adId, Newspaper newspaper);

  public Ad cancelAdInNewspaper(long adId, Newspaper newspaper);
}
