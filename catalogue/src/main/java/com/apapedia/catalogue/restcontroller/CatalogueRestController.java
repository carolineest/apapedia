package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.DTO.CatalogueMapper;
import com.apapedia.catalogue.DTO.request.CatalogueUpdateReq;
import com.apapedia.catalogue.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.DTO.response.CatalogueUpdateRes;
import com.apapedia.catalogue.jwt.JwtUtils;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.restservice.CatalogueRestService;

import java.util.UUID;
import java.util.ArrayList;
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

    @PostMapping("/add-product")
    public Catalogue addCatalogue(@RequestBody CreateCatalogueRequestDTO catalogueDTO,
                             @RequestHeader("Authorization") String authorizationHeader,
                             BindingResult bindingResult) {
        String token = authorizationHeader.substring(7);
        UUID idSeller = jwtUtils.getUserIdFromToken(token);

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            return catalogueRestService.createCatalogue(catalogueDTO, idSeller);
        }
    }

    // C3
    @GetMapping("/view-all")
    public List<Catalogue> retrieveAllCatalogue() {
        System.out.println("*************************** MASUK ***********************");
        return catalogueRestService.getAllCatalogue();
    }

    // C2
    @GetMapping("/seller")
    private List<Catalogue> getCatalogueBySellerId(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("MASUK SELLER");
        String token = authorizationHeader.substring(7);
        UUID idSeller = jwtUtils.getUserIdFromToken(token);
        System.out.println(idSeller);
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueBySellerId(idSeller);
        System.out.println(listCatalogue);
        if (listCatalogue.isEmpty()) {
            List<Catalogue> listTemp = new ArrayList<>();
            return listTemp;
        }
        return listCatalogue;
    }

    // C4
    @GetMapping("/id/{idCatalogue}")
    private Catalogue getCatalogueByCatalogueId(@PathVariable("idCatalogue") String idCatalogue) {
        Catalogue catalogue = catalogueRestService.getCatalogueByCatalogueId(UUID.fromString(idCatalogue));
        if (catalogue == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalogue with id " + idCatalogue + " not found");
        }
        return catalogue;
    }

    // C7
    @GetMapping("/name/{productName}")
    public List<Catalogue> getCatalogueByCatalogueName(@PathVariable("productName") String productName) {
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueByCatalogueName(productName);
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with name " + productName + " not found");
        }
        return listCatalogue;
    }

    @GetMapping("/login/name/{productName}")
    public List<Catalogue> getCatalogueByCatalogueNameLogin(@RequestHeader("Authorization") String authorizationHeader,
                                                            @PathVariable("productName") String productName) {
        String token = authorizationHeader.substring(7);
        UUID idSeller = jwtUtils.getUserIdFromToken(token);
        System.out.println(idSeller);

        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueByCatalogueNameLogin(productName, idSeller);
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with name " + productName + " not found");
        }
        return listCatalogue;
    }

    // C8
    // @GetMapping("/price/{price}")
    // public List<Catalogue> getCatalogueByPrice(@PathVariable("price") String
    // price){
    // List<Catalogue> listCatalogue =
    // catalogueRestService.getCatalogueByPrice(Integer.parseInt(price));
    // if (listCatalogue.isEmpty()) {
    // throw new ResponseStatusException(
    // HttpStatus.NOT_FOUND, "Product with price " + price + " not found"
    // );
    // }
    // return listCatalogue;
    // }

    @GetMapping("/price/{minPrice}/{maxPrice}")
    public List<Catalogue> getCatalogueByPrice(@PathVariable("minPrice") String minPrice,
            @PathVariable("maxPrice") String maxPrice) {
        List<Catalogue> listCatalogue = catalogueRestService.getCatalogueByPrice(Integer.parseInt(minPrice),
                Integer.parseInt(maxPrice));
        if (listCatalogue.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product with price range not found");
        }
        return listCatalogue;
    }

    // C9
    @GetMapping("/{direction}/{attribute}")
    private List<Catalogue> getCatalogueByPriceNameOrder(@PathVariable("attribute") String attribute,
            @PathVariable("direction") String direction) {
        if (attribute.equalsIgnoreCase("price") || attribute.equalsIgnoreCase("name") &&
                direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("desc")) {
            return catalogueRestService.getSortedCatalogueList(attribute, direction);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL");
    }

    @GetMapping("/update/{id}")
    public CatalogueUpdateReq updateCatalogue(@PathVariable("id") UUID id) {
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
    public Catalogue softDeleteCatalogue(@PathVariable("id") UUID id) {
        Catalogue catalogue = catalogueRestService.getCatalogueById(id);
        return catalogueRestService.softDelete(catalogue);
    }
}
