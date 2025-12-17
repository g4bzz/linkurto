package com.g4bzz.linkurto.mapper;

import com.g4bzz.linkurto.dto.UrlPostResponseBody;
import com.g4bzz.linkurto.model.Url;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UrlMapper {
    // UrlMapper singleton
    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);

    UrlPostResponseBody toUrlPostResponseBody(Url url);
}
