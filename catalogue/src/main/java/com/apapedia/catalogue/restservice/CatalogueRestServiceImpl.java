package com.apapedia.catalogue.restservice;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.repository.CatalogueDb;

@Service
@Transactional
public class CatalogueRestServiceImpl implements CatalogueRestService {
    @Autowired
    private CatalogueDb catalogueDb;
    
    @Override
    public List<Catalogue> getAllCatalogue() { return catalogueDb.findAllByOrderByProductNameAsc(); }
    
    @Override
    public List<Catalogue> getCatalogueBySellerId(UUID idSeller){
        List<Catalogue> listCatalogue = new ArrayList<>();
        for (Catalogue catalogue : catalogueDb.findAll()) {
            if (catalogue.getSeller().equals(idSeller)) {
                listCatalogue.add(catalogue);
            }
        }
        return listCatalogue;
    }

    @Override
    public Catalogue getCatalogueByCatalogueId(UUID idCatalogue){
        for (Catalogue catalogue : getAllCatalogue()) {
            if (catalogue.getId().equals(idCatalogue)) {
                return catalogue;
            }
        }
        return null;
    }

    @Override
    public List<Catalogue> getCatalogueByCatalogueName(String productName){
            List<Catalogue> listCatalogue = new ArrayList<>();
        for (Catalogue catalogue : getAllCatalogue()) {
            if (catalogue.getProductName().equalsIgnoreCase(productName)) {
                listCatalogue.add(catalogue);
            }
        }
        return listCatalogue;
    }

    @Override
    public List<Catalogue> getCatalogueByPrice(Integer price){
        List<Catalogue> listCatalogue = new ArrayList<>();
        for (Catalogue catalogue : getAllCatalogue()) {
            if (catalogue.getPrice().equals(price)) {
                listCatalogue.add(catalogue);
            }
        }
        return listCatalogue;
    }

    @Override
    public List<Catalogue> getSortedCatalogueList(String attribute, String direction){
        List<Catalogue> catalogueList = new ArrayList<>();
        if (attribute.equalsIgnoreCase("price")){
            if (direction.equalsIgnoreCase("asc")) {
                catalogueList = catalogueDb.findAllByOrderByPriceAsc();
            } else {
                catalogueList = catalogueDb.findAllByOrderByPriceDesc();
            }
        } else if (attribute.equalsIgnoreCase("name")) {
            if (direction.equalsIgnoreCase("asc")) {
                catalogueList = catalogueDb.findAllByOrderByProductNameAsc();
            } else {
                catalogueList = catalogueDb.findAllByOrderByProductNameDesc();
            }
        }
        return catalogueList;
    }
}
