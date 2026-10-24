package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;

import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.request.schedule.UpdateScheduleRequest;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;

import edu.senacsp.health_management.entity.User;

import edu.senacsp.health_management.entity.enums.ScheduleType;

import edu.senacsp.health_management.service.ScheduleService;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@CrossOrigin(origins = "${path.url}")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @PostMapping()
    @ApiMessage("Schedule successfully saved")
    public ResponseEntity<ScheduleResponse> create(@AuthenticationPrincipal User user, @RequestBody CreateScheduleRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, req));
    }

    // Get only Rules
    @GetMapping() // /schedule?profileId=1&type=MEDICATION&order=ASC
    @ApiMessage("Schedule successfully loaded")
    public ResponseEntity<List<ScheduleResponse>> findAllByProfile(@AuthenticationPrincipal User user, @RequestParam Long profileId, @RequestParam(required = false) ScheduleType type, @RequestParam(defaultValue = "DESC") Sort.Direction order)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByProfile(user, profileId, type, order));
    }

    @PutMapping
    @ApiMessage("Schedule successfully updated")
    public ResponseEntity<ScheduleResponse> update(@AuthenticationPrincipal User user, @RequestBody UpdateScheduleRequest req)
    {
        return ResponseEntity.ok(service.update(user, req));
    }

    @DeleteMapping("/{id}") // DELETE /schedule/3 | Soft delete (active = false)
    @ApiMessage("Schedule successfully deleted")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long id)
    {
        service.delete(user, id);
        return ResponseEntity.ok().build();
    }
}