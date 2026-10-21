package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;

import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;

import edu.senacsp.health_management.entity.User;

import edu.senacsp.health_management.service.ScheduleService;

import org.springframework.http.HttpStatus;
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
    @GetMapping() // /schedule?profileId=1
    @ApiMessage("Schedule successfully loaded")
    public ResponseEntity<List<ScheduleResponse>> findAllByProfile(@AuthenticationPrincipal User user, @RequestParam Long profileId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByProfile(profileId, user));
    }

    /*
     *  GET 2 ->  Consiste em passar um instant (datetime) e com base nisso extrair as ocorrências pré calculadas presentes em uma janela de tempo
     *  que se baseia no instant informado cruzando com as confirmations para retornar pro front o que exibir
     *
     *  Exemplo:
     *      - Ocorrência A (Pendente)
     *      - Ocorrência B (Confirmada)
     * */
}