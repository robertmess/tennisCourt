package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation(value = "Schedule a tennis court", notes = "This method schedules a tennis court")
    public ResponseEntity<ScheduleDTO> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {

        ScheduleDTO scheduleDTO = scheduleService.addSchedule(createScheduleRequestDTO);
        return ResponseEntity.created(locationByEntity(scheduleDTO.getId())).body(scheduleDTO);
    }

    @GetMapping
    @ApiOperation(value = "Find schedules by dates", notes = "This method finds a new schedule by date")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@ApiParam(
            name = "start date",
            type = "LocalDate",
            value = "The starting date of the schedule",
            example = "2010-06-14",
            required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @ApiParam(
                                                                          name = "end date",
                                                                          type = "LocalDate",
                                                                          value = "The end date of the schedule",
                                                                          example = "2010-06-16",
                                                                          required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/{scheduleId}")
    @ApiOperation(value = "Find schedules by id", notes = "This method finds a new schedule by id")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@ApiParam(
            name = "scheduleId",
            type = "Long",
            value = "The id of the schedule",
            example = "1",
            required = true) @PathVariable Long scheduleId) {

        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
