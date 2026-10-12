package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}