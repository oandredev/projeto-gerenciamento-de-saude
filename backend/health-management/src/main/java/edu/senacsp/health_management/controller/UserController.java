package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.profile.UpdateProfileRequest;
import edu.senacsp.health_management.dto.request.user.LoginRequest;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.dto.request.user.UpdateUserRequest;
import edu.senacsp.health_management.dto.response.profile.UpdateProfileResponse;
import edu.senacsp.health_management.dto.response.user.LoginResponse;
import edu.senacsp.health_management.dto.response.user.SignupResponse;
import edu.senacsp.health_management.dto.response.user.UpdateUserResponse;
import edu.senacsp.health_management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "${path.url}")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    @ApiMessage("Successfully created API")
    public ResponseEntity<SignupResponse> signup (@RequestBody SignupRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.signup(req));
    }

    @PostMapping("/login")
    @ApiMessage("Login successful API")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(req));
    }

    @PutMapping()
    @ApiMessage("User successfully edited")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(req));
    }
}