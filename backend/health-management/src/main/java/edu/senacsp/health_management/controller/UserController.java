package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.dto.request.LoginRequest;
import edu.senacsp.health_management.dto.request.SignupRequest;
import edu.senacsp.health_management.dto.response.LoginResponse;
import edu.senacsp.health_management.dto.response.SignupResponse;
import edu.senacsp.health_management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "${path.url}/")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup (@RequestBody SignupRequest req)
    {
        return service.signup(req);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest req)
    {
        return service.login(req);
    }
}
