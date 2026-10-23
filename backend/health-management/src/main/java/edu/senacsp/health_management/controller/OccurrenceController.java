package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;

import edu.senacsp.health_management.dto.response.occurrence.OccurrenceResponse;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.enums.ScheduleType;

import edu.senacsp.health_management.service.OccurrenceService;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/occurrence")
@CrossOrigin(origins = "${path.url}")
public class OccurrenceController {

    private final OccurrenceService service;

    public OccurrenceController(OccurrenceService service) {
        this.service = service;
    }

    @GetMapping
    @ApiMessage("Occurrence(s) successfully loaded")
    public ResponseEntity<List<OccurrenceResponse>> findWindow(@AuthenticationPrincipal User user, @RequestParam Long profileId,
                                                                     @RequestParam(required = false) ScheduleType type,
                                                                     @RequestParam(required = false) Instant reference,
                                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order)
    {
        return ResponseEntity.ok(service.findWindow(user, profileId, type, reference, order));
    }
}