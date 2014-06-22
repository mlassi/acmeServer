package com.acme.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.dom.Ad;

public interface AdRepository extends CrudRepository<Ad, Long> {

}
