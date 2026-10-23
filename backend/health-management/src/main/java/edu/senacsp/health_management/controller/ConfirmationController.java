package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;

import edu.senacsp.health_management.dto.request.confirmation.CreateConfirmationRequest;
import edu.senacsp.health_management.dto.response.confirmation.ConfirmationResponse;

import edu.senacsp.health_management.entity.User;

import edu.senacsp.health_management.service.ConfirmationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/confirmation")
@CrossOrigin(origins = "${path.url}")
public class ConfirmationController {

    private final ConfirmationService service;

    public ConfirmationController(ConfirmationService service) { this.service = service; }

    @PostMapping
    @ApiMessage("Confirmation successfully created")
    public ResponseEntity<ConfirmationResponse> create (@AuthenticationPrincipal User user, @RequestBody CreateConfirmationRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, req));
    }

    @GetMapping
    @ApiMessage("Confirmation(s) successfully loaded")
    public ResponseEntity<List<ConfirmationResponse>> findAllByProfile (@AuthenticationPrincipal User user, @RequestParam Long profileId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByProfile(user, profileId));
    }

    // TODO | Review
    @DeleteMapping("/{id}") // DELETE /confirmation/12 | No soft delete
    @ApiMessage("Confirmation successfully deleted")
    public ResponseEntity delete(@AuthenticationPrincipal User user, @PathVariable Long id)
    {
        service.delete(user, id);
        return ResponseEntity.ok().build();
    }
}