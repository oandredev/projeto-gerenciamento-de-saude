package edu.senacsp.health_management.controller;

import edu.senacsp.health_management.annotation.ApiMessage;
import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
import edu.senacsp.health_management.dto.request.profile.UpdateProfileRequest;
import edu.senacsp.health_management.dto.response.profile.CreateProfileResponse;
import edu.senacsp.health_management.dto.response.profile.ListProfileResponse;
import edu.senacsp.health_management.dto.response.profile.UpdateProfileResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CreateProfileResponse> create (@AuthenticationPrincipal User user, @RequestBody CreateProfileRequest req)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, req));
    }

    @GetMapping()
    @ApiMessage("Profile(s) successfully loaded")
    public ResponseEntity<ListProfileResponse> findAllByUser(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByUser(user));
    }

    @PutMapping()
    @ApiMessage("Profile successfully edited")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequest req)
    {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(user, req));
    }
}