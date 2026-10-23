    package edu.senacsp.health_management.controller;

    import edu.senacsp.health_management.annotation.ApiMessage;

    import edu.senacsp.health_management.dto.request.confirmation.CreateConfirmationRequest;
    import edu.senacsp.health_management.dto.response.confirmation.ConfirmationResponse;

    import edu.senacsp.health_management.entity.User;

    import edu.senacsp.health_management.service.ConfirmationService;

    import org.springframework.http.HttpStatus;
    import org.springframework.data.domain.Sort;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;

    import java.util.List;
    import java.time.Instant;

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

        @GetMapping // /confirmation?profileId=1&start=2026-10-20T03:00:00Z&end=2026-10-21T03:00:00Z&order=DESC
        @ApiMessage("Confirmation(s) successfully loaded")
        public ResponseEntity<List<ConfirmationResponse>> findByInterval(
                @AuthenticationPrincipal User user,
                @RequestParam Long profileId,
                @RequestParam Instant start,
                @RequestParam Instant end,
                @RequestParam(defaultValue = "DESC") Sort.Direction order)
        {
            return ResponseEntity.ok(service.findByInterval(user, profileId, start, end, order));
        }

        @DeleteMapping("/{id}") // DELETE /confirmation/12 | No soft delete
        @ApiMessage("Confirmation successfully deleted")
        public ResponseEntity delete(@AuthenticationPrincipal User user, @PathVariable Long id)
        {
            service.delete(user, id);
            return ResponseEntity.ok().build();
        }
    }