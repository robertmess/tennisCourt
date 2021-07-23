package com.tenniscourts.tenniscourts;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-23T12:35:18+0300",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.9.1 (Amazon.com Inc.)"
)
@Component
public class TennisCourtMapperImpl implements TennisCourtMapper {

    @Override
    public TennisCourtDTO map(TennisCourt source) {
        if ( source == null ) {
            return null;
        }

        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();

        tennisCourtDTO.setId( source.getId() );
        tennisCourtDTO.setName( source.getName() );

        return tennisCourtDTO;
    }

    @Override
    public TennisCourt map(TennisCourtDTO source) {
        if ( source == null ) {
            return null;
        }

        TennisCourt tennisCourt = new TennisCourt();

        tennisCourt.setId( source.getId() );
        tennisCourt.setName( source.getName() );

        return tennisCourt;
    }
}
