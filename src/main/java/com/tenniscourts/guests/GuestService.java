package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO findGuestById(final Long guestId) {

        return guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public GuestDTO findGuestByName(final String name) {

        Guest guest = guestRepository.findByName(name);
        return guestMapper.map(guest);
    }

    public List<GuestDTO> findAllGuests() {

        return guestMapper.map(guestRepository.findAll());
    }

    public GuestDTO addGuest(CreateGuestDTO guestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public GuestDTO updateGuest(GuestDTO guestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
}
