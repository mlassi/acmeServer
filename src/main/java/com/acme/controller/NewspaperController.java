package com.acme.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acme.dom.Newspaper;
import com.acme.service.NewspaperService;

@RestController
@RequestMapping(value = "/newspapers")
@ControllerAdvice(annotations = RestController.class)
public class NewspaperController {

  private NewspaperService newspaperService;

  @Autowired
  public NewspaperController(NewspaperService newspaperService) {
    this.newspaperService = newspaperService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<Newspaper> retrieveSingleNewspaper(@PathVariable Long id) {
    Newspaper foundNewspaper = this.newspaperService.findById(id);

    return new ResponseEntity<Newspaper>(foundNewspaper, (foundNewspaper != null) ? HttpStatus.OK
        : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = RequestMethod.GET)
  Iterable<Newspaper> retrieveAllNewspapers() {
    return this.newspaperService.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<Newspaper> addNewspaper(@RequestBody @Valid Newspaper newspaper) {
    Newspaper addedNewspaper = this.newspaperService.save(newspaper);
    return new ResponseEntity<Newspaper>(addedNewspaper, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  ResponseEntity<Newspaper> updateNewspaper(@PathVariable Long id,
      @RequestBody @Valid Newspaper newspaper) {
    Newspaper updatedNewspaper = this.newspaperService.save(newspaper);
    return new ResponseEntity<Newspaper>(updatedNewspaper,
        (updatedNewspaper != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  ResponseEntity<Long> removeNewspaper(@PathVariable Long id) {
    Newspaper deletedNewspaper = this.newspaperService.deleteById(id);

    return new ResponseEntity<Long>(id, (deletedNewspaper != null) ? HttpStatus.OK
        : HttpStatus.NOT_FOUND);
  }
}
