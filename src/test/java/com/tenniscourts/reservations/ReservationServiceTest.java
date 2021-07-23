package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ReservationMapper reservationMapper;

    Schedule schedule = Schedule.builder()
            .id(1L)
            .startDateTime(LocalDateTime.of(2021, 8, 12, 22, 11))
            .build();

    Schedule pastSchedule = Schedule.builder()
            .id(2L)
            .startDateTime(LocalDateTime.now())
            .build();

    Guest guest = Guest.builder()
            .id(1L)
            .name("Nadal")
            .build();

    Reservation reservation = Reservation.builder()
            .id(1L)
            .value(BigDecimal.valueOf(10))
            .guest(guest)
            .refundValue(BigDecimal.ZERO)
            .schedule(schedule)
            .reservationStatus(ReservationStatus.READY_TO_PLAY)
            .build();

    Reservation canceledReservation = Reservation.builder()
            .id(1L)
            .value(BigDecimal.valueOf(10))
            .guest(guest)
            .refundValue(BigDecimal.ZERO)
            .schedule(schedule)
            .reservationStatus(ReservationStatus.CANCELLED)
            .build();

    Reservation pastReservation = Reservation.builder()
            .id(1L)
            .value(BigDecimal.valueOf(10))
            .guest(guest)
            .refundValue(BigDecimal.ZERO)
            .schedule(pastSchedule)
            .reservationStatus(ReservationStatus.READY_TO_PLAY)
            .build();

    ReservationDTO mockedReservationDTO = ReservationDTO.builder()
            .id(1L)
            .reservationStatus("ready to play")
            .guestId(1L)
            .value(BigDecimal.TEN)
            .scheduledId(1L)
            .build();

    List<ReservationDTO> reservationDTOS = Collections.singletonList(mockedReservationDTO);

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        schedule.setStartDateTime(startDateTime);

        assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void getRefundValue75PercentRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(13);
        schedule.setStartDateTime(startDateTime);

        assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()).stripTrailingZeros(), new BigDecimal(7.5));
    }

    @Test
    public void getRefundValue50PercentRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(11);
        schedule.setStartDateTime(startDateTime);

        assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()).stripTrailingZeros(), new BigDecimal(5));
    }

    @Test
    public void getRefundValue25PercentRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now().plusHours(2);
        schedule.setStartDateTime(startDateTime);

        assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()).stripTrailingZeros(), new BigDecimal(2.5));
    }

    @Test
    public void getRefundValue0PercentRefund() {
        Schedule schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now();
        schedule.setStartDateTime(startDateTime);

        assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()).stripTrailingZeros(), new BigDecimal(0));
    }

    @Test(expected = EntityNotFoundException.class)
    public void bookReservationEntityNotFoundException() {
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);

        reservationService.bookReservation(createReservationRequestDTO);
    }

    @Test
    public void bookReservation() {
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO(1L, 1L);
        when(scheduleRepository.findById(any())).thenReturn(Optional.ofNullable(schedule));
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(reservationMapper.map(any(Reservation.class))).thenReturn(mockedReservationDTO);

        ReservationDTO reservationDTO = reservationService.bookReservation(createReservationRequestDTO);

        assertEquals(reservationDTO.getGuestId(),createReservationRequestDTO.getGuestId());
        assertEquals(reservationDTO.getScheduledId(),createReservationRequestDTO.getScheduleId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findReservationEntityNotFoundException() {
        reservationService.findReservation(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));

        reservationService.findReservation(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void cancelReservationEntityNotFoundException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));

        reservationService.cancelReservation(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cancelReservationCanceledReservationIllegalArgumentException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(canceledReservation));

        reservationService.cancelReservation(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cancelReservationPastReservationIllegalArgumentException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(pastReservation));

        reservationService.cancelReservation(1L);
    }

    @Test
    public void cancelReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);
        when(reservationMapper.map(any(Reservation.class))).thenReturn(mockedReservationDTO);

        ReservationDTO reservationDTO = reservationService.cancelReservation(1L);

        assertEquals(reservationDTO.getId(), mockedReservationDTO.getId());
        assertEquals(reservationDTO.getRefundValue(), mockedReservationDTO.getRefundValue());
        assertEquals(reservationDTO.getScheduledId(), mockedReservationDTO.getScheduledId());
        assertEquals(reservationDTO.getGuestId(), mockedReservationDTO.getGuestId());
    }

    @Test
    public void findAllReservations() {
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.map(anyList())).thenReturn(reservationDTOS);

        List<ReservationDTO> currentReservations = reservationService.findAllReservations();

        assertEquals(currentReservations.get(0).getId(), reservations.get(0).getId());
        assertEquals(currentReservations.get(0).getGuestId(), reservations.get(0).getGuest().getId());
        assertEquals(currentReservations.get(0).getScheduledId(), reservations.get(0).getSchedule().getId());
        assertEquals(currentReservations.get(0).getValue(), reservations.get(0).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rescheduleReservationIllegalArgumentException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        reservationService.rescheduleReservation(1L, 1L);
    }

    @Test
    public void rescheduleReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.ofNullable(reservation));
        when(reservationRepository.save(any())).thenReturn(pastReservation);
        when(scheduleRepository.findById(any())).thenReturn(Optional.ofNullable(schedule));
        when(reservationMapper.map(any(Reservation.class))).thenReturn(mockedReservationDTO);

        ReservationDTO rescheduledReservation = reservationService.rescheduleReservation(1L, 1L);

        assertEquals(rescheduledReservation.getId(), mockedReservationDTO.getId());
        assertEquals(rescheduledReservation.getValue(), mockedReservationDTO.getValue());
        assertEquals(rescheduledReservation.getReservationStatus(), mockedReservationDTO.getReservationStatus());
        assertEquals(rescheduledReservation.getRefundValue(), mockedReservationDTO.getRefundValue());
    }

}