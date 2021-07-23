package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation(value = "Book a reservation", notes = "This method creates a new reservation")
    public ResponseEntity<ReservationDTO> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {

        ReservationDTO reservationDTO = reservationService.bookReservation(createReservationRequestDTO);
        return ResponseEntity.created(locationByEntity(reservationDTO.getId())).body(reservationDTO);
    }

    @GetMapping("/{reservationId}")
    @ApiOperation(value = "Find a reservation", notes = "This method finds an existing reservation by reservation id")
    public ResponseEntity<ReservationDTO> findReservation(@ApiParam(
            name = "reservationId",
            type = "Long",
            value = "The id of the reservation",
            example = "1",
            required = true) @PathVariable Long reservationId) {

        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PatchMapping("/{reservationId}/cancel")
    @ApiOperation(value = "Cancel a reservation", notes = "This method cancels an existing reservation by reservation id")
    public ResponseEntity<ReservationDTO> cancelReservation(@ApiParam(
            name = "reservationId",
            type = "Long",
            value = "The id of the reservation",
            example = "1",
            required = true) @PathVariable Long reservationId) {

        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/{reservationId}/schedule/{scheduleId}")
    @ApiOperation(value = "Reschedule a reservation",
            notes = "This method cancels an existing reservation and creates a new one by reservation id and schedule id")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@ApiParam(
            name = "reservationId",
            type = "Long",
            value = "The id of the reservation",
            example = "1",
            required = true) @PathVariable Long reservationId, @ApiParam(
            name = "scheduleId",
            type = "Long",
            value = "The id of the schedule",
            example = "1",
            required = true) @PathVariable Long scheduleId) {

        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> findAllReservations() {

        return ResponseEntity.ok(reservationService.findAllReservations());
    }
}
