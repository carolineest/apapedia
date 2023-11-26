package com.apapedia.catalogue.repository;

import com.apapedia.catalogue.model.Catalogues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CatalogueDB extends JpaRepository<Catalogues, UUID> {
}