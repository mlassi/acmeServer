package com.acme.test.builder;

import org.springframework.test.util.ReflectionTestUtils;

import com.acme.dom.Ad;

public class AdBuilder {

  private Ad ad = new Ad();

  public static AdBuilder anAd() {
    return new AdBuilder();
  }

  public AdBuilder withId(long id) {
    ReflectionTestUtils.setField(ad, "id", id);
    return this;
  }

  public AdBuilder withAdTitle(String title) {
    this.ad.setTitle(title);
    return this;
  }

  public AdBuilder withAdDescription(String description) {
    this.ad.setDescription(description);
    return this;
  }

  // public AdBuilder withPublishOnDate(Date publishOnDate) {
  // this.ad.setPublishOnDate(publishOnDate);
  // return this;
  // }

  public Ad build() {
    return this.ad;
  }
}
