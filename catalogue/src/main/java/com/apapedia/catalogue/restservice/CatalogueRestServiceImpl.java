package com.apapedia.catalogue.restservice;

import com.apapedia.catalogue.DTO.response.CatalogueFilterDTO;
import com.apapedia.catalogue.repository.CatalogueDb;
import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.DTO.request.CatalogueUpdateReq;
import com.apapedia.catalogue.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.DTO.response.CatalogueUpdateRes;
import com.apapedia.catalogue.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CatalogueRestServiceImpl implements CatalogueRestService {
    @Autowired
    private CatalogueDb catalogueDb;

    @Autowired
    private CategoryRestService categoryService;
    
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
        return catalogueDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(productName);
    }

    @Override
    public List<Catalogue> getCatalogueByCatalogueNameLogin(String productName, UUID sellerId){
        return catalogueDb.findByProductNameContainingIgnoreCaseAndSeller(productName, sellerId);
    }

    @Override
    public List<Catalogue> getCatalogueByPrice(Integer minPrice, Integer maxPrice){
        List<Catalogue> listCatalogue = new ArrayList<>();
        for (Catalogue catalogue : getAllCatalogue()) {
            if (catalogue.getPrice() <= maxPrice && catalogue.getPrice() >= minPrice) {
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

    @Override
    public List<Catalogue> getAllCatalogues(){
        return catalogueDb.findAll();
    }
    @Override
    public Catalogue getCatalogueById(UUID id){
        for (Catalogue catalogue : getAllCatalogues()){
            if (catalogue.getId().equals(id)){
                return catalogue;
            }
        }
        return null;
    }

    @Override //handle image dan category!!
    public Catalogue updateCatalogue(CatalogueUpdateRes catalogueUpdateRes, UUID id){
        Catalogue catalogue = getCatalogueById(id);
        Category category = categoryService.getCategoryById(catalogueUpdateRes.getCategoryId());
        if(catalogue != null){
            if(catalogueUpdateRes.getProductName()!=null){
                catalogue.setProductName(catalogueUpdateRes.getProductName());
            }
            if(catalogueUpdateRes.getPrice()!=null){
                catalogue.setPrice(catalogueUpdateRes.getPrice());
            }
            if(catalogueUpdateRes.getProductDescription()!=null){
                catalogue.setProductDescription(catalogueUpdateRes.getProductDescription());
            }
            if(catalogueUpdateRes.getStock()!=null){
                catalogue.setStock(catalogueUpdateRes.getStock());
            }
//            catalogue.setImage(catalogueUpdateReq.getImage());
            catalogue.setCategoryId(category);
            catalogueDb.save(catalogue);
        }
        return catalogue;
    }

    @Override
    public Catalogue softDelete(Catalogue catalogue) {
        catalogue.setDeleted(true);
        return catalogueDb.save(catalogue);
    }

    @Override
    public void createCatalogue(CreateCatalogueRequestDTO catalogueDTO, UUID sellerId) {
        Catalogue catalogue = new Catalogue();
        catalogue.setSeller(sellerId);
        catalogue.setPrice(catalogueDTO.getPrice());
        catalogue.setProductName(catalogueDTO.getProductName());
        catalogue.setProductDescription(catalogueDTO.getProductDescription());

        String catId = catalogueDTO.getCategoryId();
        Category category = categoryService.getCategoryById(UUID.fromString(catId));
        catalogue.setCategoryId(category);
        
        catalogue.setStock(catalogueDTO.getStock());
        catalogue.setImage(catalogueDTO.getImage());
        catalogueDb.save(catalogue);
    }
}
