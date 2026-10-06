package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
import edu.senacsp.health_management.dto.request.profile.UpdateProfileRequest;
import edu.senacsp.health_management.dto.response.profile.ProfileResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "${path.url}")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping()
    @ApiMessage("Profile successfully created")
    public ResponseEntity<ProfileResponse> create (@AuthenticationPrincipal User user, @RequestBody CreateProfileRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, req));
    }

    @GetMapping()
    @ApiMessage("Profile(s) successfully loaded")
    public ResponseEntity<List<ProfileResponse>> findAllByUser(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByUser(user));
    }

    @PutMapping()
    @ApiMessage("Profile successfully edited")
    public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(user, req));
    }
}