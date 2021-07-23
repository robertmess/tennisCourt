package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;
    private final TennisCourtRepository tennisCourtRepository;


    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourt tennisCourt = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Tennis court for given id %s was not found", createScheduleRequestDTO.getTennisCourtId())));
        Schedule schedule = Schedule.builder()
                .tennisCourt(tennisCourt)
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1))
                .build();

        return scheduleMapper.map(schedule);
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
         List<Schedule> schedules = scheduleRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate);

         return scheduleMapper.map(schedules);
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        Schedule schedule =  scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Schedule for given id %s was not found", scheduleId)));

        return scheduleMapper.map(schedule);
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {

        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
