package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.service.MedicationScheduleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@CrossOrigin(origins = "${path.url}")
public class MedicationScheduleController {

    private final MedicationScheduleService service;

    public MedicationScheduleController(MedicationScheduleService service) {
        this.service = service;
    }
}