package com.nissum.security.mapper;

import com.nissum.security.domain.dto.PhonesDTO;
import com.nissum.security.domain.entities.Phones;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhonesMapper {

    PhonesDTO toDto(Phones phones);

    Phones toEntity(PhonesDTO phonesDTO);
}
