package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.service.AppointmentScheduleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@CrossOrigin(origins = "${path.url}")
public class AppointmentScheduleController {

    private final AppointmentScheduleService service;

    public AppointmentScheduleController(AppointmentScheduleService service) {
        this.service = service;
    }
}