package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.request.LoginRequest;
import edu.senacsp.health_management.dto.request.SignupRequest;
import edu.senacsp.health_management.dto.response.LoginResponse;
import edu.senacsp.health_management.dto.response.SignupResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public ResponseEntity<SignupResponse> signup (@RequestBody SignupRequest req) {

        if (repo.existsByEmail(req.email()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        else
        {
            User newUserData = new User(req.name(), req.email(), req.password()); // TODO - Add encryption in Password

            repo.save(newUserData);

            return ResponseEntity.status(HttpStatus.CREATED).build(); // Return only Code 201 | Successfully created
        }
    }

    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest req)
    {
        Optional<User> userDB = repo.findByEmail(req.email());

        // Can Access
        if (userDB.isPresent() /* TODO Check also if Password is valid */)
        {
            User user = userDB.get(); // Cast

            return ResponseEntity.ok(new LoginResponse(user.getName(), user.getEmail(), "123TOKEN")); // TODO - Add token logic
        }
        // Acess Denied
        else
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid");
        }
    }
}