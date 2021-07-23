package com.tenniscourts.guests;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest map(GuestDTO source);

    Guest map(CreateGuestDTO source);

    GuestDTO map(Guest source);

    List<GuestDTO> map(List<Guest> source);
}
