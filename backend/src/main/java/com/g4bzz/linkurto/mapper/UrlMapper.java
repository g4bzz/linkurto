package com.g4bzz.linkurto.mapper;

import com.g4bzz.linkurto.dto.UrlPostResponseBody;
import com.g4bzz.linkurto.model.Url;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UrlMapper {
    // UrlMapper singleton
    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);

    UrlPostResponseBody toUrlPostResponseBody(Url url);
}
