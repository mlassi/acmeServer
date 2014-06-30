package com.acme.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.dom.exception.PublishAdException;
import com.acme.repository.AdRepository;

@Service
public class AdRepositoryService implements AdService {

  private AdRepository adRepository;

  @Autowired
  public AdRepositoryService(AdRepository adRepository) {
    this.adRepository = adRepository;
  }

  @Override
  public Ad findById(long id) {
    return this.adRepository.findOne(id);
  }

  @Override
  public Collection<Ad> findAll() {
    return (Collection<Ad>) this.adRepository.findAll();
  }

  @Transactional(rollbackFor = {Exception.class})
  @Override
  public Ad save(Ad ad) {
    return this.adRepository.save(ad);
  }

  @Transactional(rollbackFor = {Exception.class})
  @Override
  public Ad deleteById(long id) {
    Ad foundAd = this.findById(id);
    this.adRepository.delete(foundAd);
    return foundAd;
  }

  @Transactional(rollbackFor = {Exception.class})
  @Override
  public Ad postAdToNewspaper(long adId, Newspaper newspaper) throws PublishAdException {
    Ad foundAd = findById(adId);
    if (foundAd == null) {
      throw new PublishAdException(String.format("Could not find ad by id %s", adId));
    }
    foundAd.addNewspaper(newspaper);
    return this.adRepository.save(foundAd);
  }

  @Transactional(rollbackFor = {Exception.class})
  @Override
  public Ad cancelAdInNewspaper(long adId, Newspaper newspaper) throws PublishAdException {
    Ad foundAd = this.adRepository.findOne(adId);
    if (foundAd == null) {
      throw new PublishAdException(String.format("Could not find ad by id %s", adId));
    }
    foundAd.removeNewspaper(newspaper);
    return this.adRepository.save(foundAd);
  }

}
