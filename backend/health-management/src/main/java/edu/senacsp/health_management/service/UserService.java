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
    import org.springframework.web.server.ResponseStatusException;

    @Service
    public class UserService {

        private final UserRepository repo;

        public UserService(UserRepository repo) {
            this.repo = repo;
        }

        /**
         * @param req the signup credentials
         * @return a {@link ResponseEntity} containing the {@link SignupResponse}
         * @throws ResponseStatusException if email already in use (CODE 409)
         */
        public ResponseEntity<SignupResponse> signup (SignupRequest req) {

            if (repo.existsByEmail(req.email()))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            User newUser = new User(req.name(), req.email(), req.password());
            repo.save(newUser);

            return ResponseEntity.status(
                    HttpStatus.CREATED)
                    .body(new SignupResponse(newUser.getId(), newUser.getName(), newUser.getEmail())); // 201 | Successfully created
        }

        /**
         *  Authenticates a user by email and password
         *  @param req the login credentials
         *  @return a {@link ResponseEntity} containing the authenticated user's data
         *  @throws ResponseStatusException if email or password is invalid (CODE 401)
         */
        public ResponseEntity<LoginResponse> login (LoginRequest req)
        {
            /* Same message for email not found and wrong password to avoid leaking whether an email is registered */
            User user = repo.findByEmail(req.email())
                    .filter(userDB -> userDB.getPassword().equals(req.password()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid")); // 401

            // TODO - Add token JWT logic in FUTURE

            return ResponseEntity.ok(new LoginResponse(user.getEmail(), user.getName())); // 200
        }
    }