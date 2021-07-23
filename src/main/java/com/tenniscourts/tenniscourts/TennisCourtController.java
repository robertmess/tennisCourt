package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/tennisCourt")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping
    @ApiOperation(value = "Add a new tennis court", notes = "This method creates a new tennis court")
    public ResponseEntity<TennisCourtDTO> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {

        TennisCourtDTO newTennisCourtDTO = tennisCourtService.addTennisCourt(tennisCourtDTO);
        return ResponseEntity.created(locationByEntity(newTennisCourtDTO.getId())).body(newTennisCourtDTO);
    }

    @GetMapping("/{tennisCourtId}")
    @ApiOperation(value = "Find tennis court by court id", notes = "This method finds a new tennis court by id")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@ApiParam(
            name = "tennis court id",
            type = "Long",
            value = "The tennis court id",
            example = "1",
            required = true) @PathVariable Long tennisCourtId) {

        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @GetMapping("/{tennisCourtId}/schedule")
    @ApiOperation(value = "Find tennis court by dates", notes = "This method finds a new tennis court with schedules by id")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@ApiParam(
            name = "tennis court id",
            type = "Long",
            value = "The tennis court id",
            example = "1",
            required = true) @PathVariable Long tennisCourtId) {

        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
