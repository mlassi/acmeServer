package com.acme.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.dom.Newspaper;
import com.acme.repository.NewspaperRepository;

@Service
public class NewspaperRepositoryService implements NewspaperService {

  private NewspaperRepository newspaperRepository;

  @Autowired
  public NewspaperRepositoryService(NewspaperRepository newspaperRepository) {
    this.newspaperRepository = newspaperRepository;
  }

  @Override
  public Newspaper findById(long id) {
    return this.newspaperRepository.findOne(id);
  }

  @Override
  public Collection<Newspaper> findAll() {
    return (Collection<Newspaper>) this.newspaperRepository.findAll();
  }

  @Override
  public Newspaper save(Newspaper newspaper) {
    return this.newspaperRepository.save(newspaper);
  }

  @Override
  public Newspaper deleteById(long id) {
    Newspaper deletingNewspaper = this.findById(id);
    this.newspaperRepository.delete(deletingNewspaper);
    return deletingNewspaper;
  }

}
