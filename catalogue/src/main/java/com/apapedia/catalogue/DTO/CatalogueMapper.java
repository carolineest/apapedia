package com.apapedia.catalogue.DTO;

import com.apapedia.catalogue.DTO.request.CatalogueUpdateReq;
import com.apapedia.catalogue.model.Catalogue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogueMapper {
    CatalogueUpdateReq updateCatalogueToCatalogueUpdateReq(Catalogue catalogue);
}
