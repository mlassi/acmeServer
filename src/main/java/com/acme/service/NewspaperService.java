package com.acme.service;

import java.util.Collection;

import com.acme.dom.Newspaper;

public interface NewspaperService {

  /**
   * Finds a newspaper by it's Newspaper id. If none is found, null is returned.
   * 
   * @param id
   * @return The found Newspaper or null if it wasn't found
   */
  public Newspaper findById(long id);

  /**
   * Returns all the newspapers in the system
   * 
   * @return A collection of newspapers or empty if none was found
   */
  public Collection<Newspaper> findAll();

  /**
   * Saves or updates the Newspaper passed in. It will determine whether it's a new entity or
   * existing entity.
   * 
   * @param newspaper
   * @return The saved newspaper
   */
  public Newspaper save(Newspaper newspaper);

  /**
   * Removes the Newspaper if it was found.
   * 
   * @param id
   * @return the newspaper that was deleted
   */
  public Newspaper deleteById(long id);

}
