package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.model.Catalogue;

public interface CatalogueRestService {
    List<Catalogue> getAllCatalogue();
    List<Catalogue> getCatalogueBySellerId(UUID idSeller);
    Catalogue getCatalogueByCatalogueId(UUID idCatalogue);
    List<Catalogue> getCatalogueByCatalogueName(String productName);
    List<Catalogue> getCatalogueByPrice(Integer price);
    List<Catalogue> getSortedCatalogueList(String attribute, String direction);
}
