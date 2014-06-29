package com.acme.test.builder;

import org.springframework.test.util.ReflectionTestUtils;

import com.acme.dom.Newspaper;

public class NewspaperBuilder {

  Newspaper newspaper = new Newspaper();

  public static NewspaperBuilder aNewspaper() {
    return new NewspaperBuilder();
  }

  public NewspaperBuilder withPublicationName(String publicationName) {
    this.newspaper.setPublicationName(publicationName);
    return this;
  }

  public NewspaperBuilder withId(long id) {
    ReflectionTestUtils.setField(newspaper, "id", id);
    return this;
  }

  public Newspaper build() {
    return this.newspaper;
  }
}
