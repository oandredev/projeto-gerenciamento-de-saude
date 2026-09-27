package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.restriction.CreateRestrictionRequest;
import edu.senacsp.health_management.dto.request.restriction.UpdateRestrictionRequest;
import edu.senacsp.health_management.dto.response.restriction.CreateRestrictionResponse;
import edu.senacsp.health_management.dto.response.restriction.ListRestrictionResponse;
import edu.senacsp.health_management.dto.response.restriction.UpdateRestrictionResponse;
import edu.senacsp.health_management.service.RestrictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CreateRestrictionResponse> create (@RequestBody CreateRestrictionRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/profile/{profileId}")
    @ApiMessage("Restriction(s) successfully loaded")
    public ResponseEntity<ListRestrictionResponse> findAllByProfile(@PathVariable Long profileId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByProfile(profileId));
    }

    @PutMapping()
    @ApiMessage("Restriction successfully edited")
    public ResponseEntity<UpdateRestrictionResponse> updateRestriction(@RequestBody UpdateRestrictionRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateRestriction(req));
    }

}