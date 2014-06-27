package com.acme.dom;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

@Entity
@Table(name = "ads")
public class Ad implements Identifiable<Long>, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "title")
  @NotEmpty
  private String title;

  @NotEmpty
  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "ads_newspapers", joinColumns = {@JoinColumn(name = "ads_id",
      referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "newspapers_id",
      referencedColumnName = "id")})
  private Set<Newspaper> newspapers;
  // private AdCategoryType adCategoryType;
  // private Date publishOnDate;

  public Ad() {

  }

  public Set<Newspaper> getNewspapers() {
    return this.newspapers;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public Date getCurrentDate() {
	  return Calendar.getInstance().getTime();
  }

  // public Set<Newspaper> getNewspapers() {
  // return this.newspapers;
  // }

  public void addNewspaper(Newspaper newspaper) {
	if(this.newspapers == null) {
		this.newspapers = new HashSet<Newspaper>();
	}
    this.newspapers.add(newspaper);
  }

	public void removeNewspaper(Newspaper newspaper) {
		if (this.newspapers == null) {
			this.newspapers = new HashSet<Newspaper>();
		}
		this.newspapers.remove(newspaper);
	}

  /*
   * public AdCategoryType getAdCategoryType() { return adCategoryType; }
   * 
   * public Date getPublishOnDate() { return publishOnDate; }
   */


  /*
   * 
   * 
   * public void setAdCategoryType(AdCategoryType adCategoryType) { this.adCategoryType =
   * adCategoryType; }
   * 
   * public void setPublishOnDate(Date publishOnDate) { this.publishOnDate = publishOnDate; }
   */



}
