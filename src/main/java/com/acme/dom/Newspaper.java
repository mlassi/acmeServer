package com.acme.dom;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

@Entity
@Table(name = "newspapers")
public class Newspaper implements Identifiable<Long>, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "name")
  @NotEmpty
  private String publicationName;


  @ManyToMany(mappedBy = "newspapers")
  private Set<Ad> ads;

  @Override
  public Long getId() {
    return this.id;
  }

  public String getPublicationName() {
    return this.publicationName;
  }

  public void setPublicationName(String publicationName) {
    this.publicationName = publicationName;
  }


}
