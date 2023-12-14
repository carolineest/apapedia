package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.DTO.request.CatalogueUpdateReq;
import com.apapedia.catalogue.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.DTO.response.CatalogueUpdateRes;
import com.apapedia.catalogue.model.Catalogue;

public interface CatalogueRestService {
    List<Catalogue> getAllCatalogue();
    List<Catalogue> getCatalogueBySellerId(UUID idSeller);
    Catalogue getCatalogueByCatalogueId(UUID idCatalogue);
    List<Catalogue> getCatalogueByCatalogueName(String productName);
    List<Catalogue> getCatalogueByCatalogueNameLogin(String productName, UUID sellerId);
    List<Catalogue> getCatalogueByPrice(Integer minPrice, Integer maxPrice);
    List<Catalogue> getSortedCatalogueList(String attribute, String direction);
    List<Catalogue> getAllCatalogues();
    Catalogue getCatalogueById(UUID id);
    Catalogue updateCatalogue(CatalogueUpdateRes catalogueUpdateRes, UUID id);
    Catalogue softDelete(Catalogue catalogue);
    Catalogue createCatalogue(CreateCatalogueRequestDTO catalogueDTO, UUID selllerId);
}
