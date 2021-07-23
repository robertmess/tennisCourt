package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private GuestService guestService;

    @GetMapping("/{guestId}")
    @ApiOperation(value = "Find a guest", notes = "This method finds an existing guest by guest id")
    public ResponseEntity<GuestDTO> findGuest(@ApiParam(
            name = "guestId",
            type = "Long",
            value = "The id of the guest",
            example = "1",
            required = true) @PathVariable Long guestId) {

        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping("/name/{name}")
    @ApiOperation(value = "Find a guest", notes = "This method finds an existing guest by name")
    public ResponseEntity<GuestDTO> findGuest(@ApiParam(
            name = "name",
            type = "String",
            value = "The name of the guest",
            example = "Nadal",
            required = true) @PathVariable String name) {

        return ResponseEntity.ok(guestService.findGuestByName(name));
    }

    @GetMapping
    @ApiOperation(value = "Find all guests", notes = "This method finds all existing guests")
    public ResponseEntity<List<GuestDTO>> findAllGuests() {

        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @PostMapping
    @ApiOperation(value = "Create a guest", notes = "This method creates a new guest")
    public ResponseEntity<GuestDTO> addGuest(@Validated @RequestBody CreateGuestDTO guestDTO){

        GuestDTO newGuest = guestService.addGuest(guestDTO);
        return ResponseEntity.created(locationByEntity(newGuest.getId())).body(newGuest);
    }

    @PutMapping
    @ApiOperation(value = "Find a guest", notes = "This method updates an existing guest")
    public ResponseEntity<GuestDTO> updateGuest(@Validated @RequestBody GuestDTO guestDTO){

        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @DeleteMapping("/{guestId}")
    @ApiOperation(value = "Delete a guest", notes = "This method deletes an existing guest by guest id")
    public ResponseEntity<Void> deleteGuest(@ApiParam(
            name = "guestId",
            type = "Long",
            value = "The id of the guest",
            example = "1",
            required = true) @PathVariable Long guestId){

        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
