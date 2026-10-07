package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.restriction.CreateRestrictionRequest;
import edu.senacsp.health_management.dto.request.restriction.UpdateRestrictionRequest;
import edu.senacsp.health_management.dto.response.restriction.RestrictionResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.service.RestrictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restriction")
@CrossOrigin(origins = "${path.url}")
public class RestrictionController {

    private final RestrictionService service;

    public RestrictionController(RestrictionService service) {
        this.service = service;
    }

    @PostMapping()
    @ApiMessage("Successfully created a new restriction")
    public ResponseEntity<RestrictionResponse> create (@AuthenticationPrincipal User user, @RequestBody CreateRestrictionRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, req));
    }

    @GetMapping() // .../restriction?profileId=1
    @ApiMessage("Restriction(s) successfully loaded")
    public ResponseEntity<List<RestrictionResponse>> findAllByProfile(@AuthenticationPrincipal User user, @RequestParam Long profileId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByProfile(user, profileId));
    }

    @PutMapping("/{id}")
    @ApiMessage("Restriction successfully edited")
    public ResponseEntity<RestrictionResponse> update(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody UpdateRestrictionRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(user, id, req));
    }
}