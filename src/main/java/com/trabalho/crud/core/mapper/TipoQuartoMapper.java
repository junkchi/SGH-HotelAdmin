package com.trabalho.crud.core.mapper;

import org.mapstruct.Mapper;
import com.trabalho.crud.core.dto.TipoQuartoDto;
import com.trabalho.crud.core.entity.TipoQuarto;

@Mapper(componentModel = "spring")
public interface TipoQuartoMapper {

    TipoQuarto toEntity(TipoQuartoDto dto);

    TipoQuartoDto toDto(TipoQuarto entity);

}