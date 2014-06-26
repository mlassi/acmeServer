package com.acme.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acme.dom.Ad;
import com.acme.dom.Newspaper;
import com.acme.dom.exception.PublishAdException;
import com.acme.service.AdService;

@RestController
@RequestMapping(value = "/ads")
public class AdController {

  AdService adService;

  @Autowired
  public AdController(AdService adService) {
    this.adService = adService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<Ad> retriveSingleAd(@PathVariable Long id) {
    Ad returnedAd = this.adService.findById(id);
    return new ResponseEntity<Ad>(returnedAd, (returnedAd != null) ? HttpStatus.OK
        : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = GET)
  Collection<Ad> retrieveAllAdsAd() {
    return (Collection<Ad>) this.adService.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<Ad> addAd(@Valid @RequestBody Ad ad) {
    Ad returnedAd = this.adService.save(ad);
    return new ResponseEntity<Ad>(returnedAd, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  ResponseEntity<Ad> updateAd(@PathVariable Long id, @Valid @RequestBody Ad ad) {
    Ad returnedAd = this.adService.save(ad);
    return new ResponseEntity<Ad>(returnedAd, (returnedAd != null) ? HttpStatus.OK
        : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  ResponseEntity<Long> removedAd(@PathVariable long id) {
    Ad removedAd = this.adService.deleteById(id);

    return new ResponseEntity<Long>(id, (removedAd != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/{id}/newspapers/{newspaper}")
  ResponseEntity<Ad> postAdToNewspaper(@PathVariable Long id, @RequestBody Newspaper newspaper) throws PublishAdException {
    Ad associatedAd = this.adService.postAdToNewspaper(id, newspaper);
    return new ResponseEntity<Ad>(associatedAd, (associatedAd != null) ? HttpStatus.CREATED
        : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/newspapers/{newspaper}")
  ResponseEntity<Ad> cancelAdInNewspaper(@PathVariable Long id, @RequestBody Newspaper newspaper) throws PublishAdException {
    Ad cancelledAd = this.adService.cancelAdInNewspaper(id, newspaper);
    return new ResponseEntity<Ad>(cancelledAd, (cancelledAd != null) ? HttpStatus.OK
        : HttpStatus.NOT_FOUND);
  }



}
