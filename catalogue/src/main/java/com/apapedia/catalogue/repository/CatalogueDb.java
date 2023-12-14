package com.apapedia.catalogue.repository;

import com.apapedia.catalogue.model.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatalogueDb extends JpaRepository<Catalogue, UUID> {
    List<Catalogue> findAllByOrderByPriceAsc();
    List<Catalogue> findAllByOrderByPriceDesc();
    List<Catalogue> findAllByOrderByProductNameAsc();
    List<Catalogue> findAllByOrderByProductNameDesc();
    List<Catalogue> findByProductNameContainingIgnoreCaseOrderByProductNameAsc(String productName);
    List<Catalogue> findByProductNameContainingIgnoreCaseAndSeller(String productName, UUID sellerId);
}
