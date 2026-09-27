package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.service.RestrictionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restriction")
@CrossOrigin(origins = "${path.url}")
public class RestrictionController {

    private final RestrictionService service;

    public RestrictionController(RestrictionService service) {
        this.service = service;
    }
}