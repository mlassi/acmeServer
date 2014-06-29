package com.acme.service;

import java.util.Collection;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.dom.exception.PublishAdException;

public interface AdService {

  /**
   * Finds an Ad by it's Ad id. If none is found, null is returned.
   * 
   * @param id, the ad id of the Ad we want to find
   * @return The Ad or null if no Ad is found
   */
  public Ad findById(long id);

  /**
   * Returns all ads from the system
   * 
   * @return An iterable of ads
   */
  public Collection<Ad> findAll();

  /**
   * Saves or updates the Ad passed in. It will determine whether it's a new entity or existing
   * entity.
   * 
   * @param ad
   * @return the persisted Ad
   */
  public Ad save(Ad ad);

  /**
   * Removes the Ad if it was found.
   * 
   * @param id
   * @return the Ad that was deleted or null if no add was found.
   */
  public Ad deleteById(long id);

  /**
   * Posts an ad to a newspaper and associates the two entites with each other.
   * 
   * @param adId
   * @param newspaper
   * @return The persisted ad if both the ad and newspaper exists
   * @throws PublishAdException If no ad and/or newspaper was found, the PublishAdException is
   *         thrown.
   */
  public Ad postAdToNewspaper(long adId, Newspaper newspaper) throws PublishAdException;

  /**
   * If the ad hasn't been published in the newspaper yet, the cancel will undo the planned publish
   * of the ad and removes the association between the two entities.
   * 
   * @param adId
   * @param newspaper
   * @return The persisted ad if both the ad and newspaper exists
   * @throws PublishAdException PublishAdException If no ad and/or newspaper was found, the
   *         PublishAdException is thrown.
   */
  public Ad cancelAdInNewspaper(long adId, Newspaper newspaper) throws PublishAdException;
}
