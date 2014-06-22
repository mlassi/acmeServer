package com.acme.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.dom.Newspaper;

public interface NewspaperRepository extends CrudRepository<Newspaper, Long> {

}
