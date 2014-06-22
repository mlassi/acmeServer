package com.acme.service;

import java.util.Collection;

import com.acme.dom.Newspaper;

public interface NewspaperService {

  public Newspaper findById(long id);

  public Collection<Newspaper> findAll();

  public Newspaper save(Newspaper newspaper);

  public Newspaper deleteById(long id);

}
