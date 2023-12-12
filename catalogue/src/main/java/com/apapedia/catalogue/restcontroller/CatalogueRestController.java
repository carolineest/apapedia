package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.DTO.CatalogueMapper;
import com.apapedia.catalogue.DTO.request.CatalogueUpdateReq;
import com.apapedia.catalogue.DTO.response.CatalogueUpdateRes;
import com.apapedia.catalogue.jwt.JwtUtils;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.restservice.CatalogueRestService;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueRestController {
    @Autowired
    private CatalogueRestService catalogueRestService;
    @Autowired
    private CatalogueMapper catalogueMapper;
    @Autowired
    JwtUtils jwtUtils;

    // C3
    @GetMapping("/view-all")
    public List<Catalogue> retrieveAllCatalogue(){
        return catalogueRestService.getAllCatalogue();
    }

    // C2
    // @GetMapping("/seller/{idSeller}")
    // private List<Catalogue> getCatalogueBySellerId(@PathVariable("idSeller") String idSeller){
    //     List<Catalogue> listCatalogue = catalogueRestService.getCatalogueBySellerId(UUID.fromString(idSeller));
    //     if (listCatalogue.isEmpty()) {
    //         throw new ResponseStatusException(
    //             HttpStatus.NOT_FOUND, "Seller with id " + idSeller + " not found"
    //         );
    //     }  
    //     return listCatalogue;
    // }
    @GetMapping("/seller")
    private List<Catalogue> getCatalogueBySellerId(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        UUID idSeller = jwtUtils.getUserIdFromToken(token);
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueBySellerId(idSeller);
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Seller with id " + idSeller + " not found"
            );
        }  
        return listCatalogue;
    }

    // C4
    @GetMapping("/id/{idCatalogue}")
    private Catalogue getCatalogueByCatalogueId(@PathVariable("idCatalogue") String idCatalogue){
        Catalogue catalogue = catalogueRestService.getCatalogueByCatalogueId(UUID.fromString(idCatalogue));
        if (catalogue == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Catalogue with id " + idCatalogue + " not found"
            );
        }  
        return catalogue;
    }

    // C7
    @GetMapping("/name/{productName}")
    public List<Catalogue> getCatalogueByCatalogueName(@PathVariable("productName") String productName){
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueByCatalogueName(productName);
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product with name " + productName + " not found"
            );
        }  
        return listCatalogue;
    }

    // C8
    @GetMapping("/price/{price}")
    public List<Catalogue> getCatalogueByPrice(@PathVariable("price") String price){
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueByPrice(Integer.parseInt(price));
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product with price " + price + " not found"
            );
        }  
        return listCatalogue;
    }

    // C9
    @GetMapping("/{direction}/{attribute}")
    private List<Catalogue> getCatalogueByPriceNameOrder(@PathVariable("attribute") String attribute, @PathVariable("direction") String direction) {
        if (attribute.equalsIgnoreCase("price") || attribute.equalsIgnoreCase("name") &&
            direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("desc")) {
            return catalogueRestService.getSortedCatalogueList(attribute, direction);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL");
    }

    @GetMapping("/update/{id}")
    public CatalogueUpdateReq updateCatalogue(@PathVariable("id") UUID id){
        Catalogue catalogue = catalogueRestService.getCatalogueById(id);
        return catalogueMapper.updateCatalogueToCatalogueUpdateReq(catalogue);
    }

    @PutMapping("/update/{id}")
    public Catalogue updateCatalogue(@PathVariable("id") UUID id,
                                      @Valid @RequestBody CatalogueUpdateRes catalogueUpdateRes) {
        Catalogue catalogue = catalogueRestService.updateCatalogue(catalogueUpdateRes, id);
        return catalogue;
    }

    @GetMapping("/softdelete/{id}")
    public Catalogue softDeleteCatalogue(@PathVariable("id") UUID id){
        Catalogue catalogue = catalogueRestService.getCatalogueById(id);
        return catalogueRestService.softDelete(catalogue);
    }
}
