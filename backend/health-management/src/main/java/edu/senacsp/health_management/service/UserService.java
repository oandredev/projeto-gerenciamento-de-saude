    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.user.LoginRequest;
    import edu.senacsp.health_management.dto.request.user.SignupRequest;
    import edu.senacsp.health_management.dto.request.user.UpdateUserRequest;
    import edu.senacsp.health_management.dto.response.user.LoginResponse;
    import edu.senacsp.health_management.dto.response.user.SignupResponse;
    import edu.senacsp.health_management.dto.response.user.UpdateUserResponse;
    import edu.senacsp.health_management.entity.User;
    import edu.senacsp.health_management.repository.UserRepository;
    import org.springframework.http.HttpStatus;
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
         * @return a @{SignupResponse}
         * @throws ResponseStatusException if email already in use (CODE 409)
         */
        public SignupResponse signup (SignupRequest req) {

            if (repo.existsByEmail(req.email()))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            User newUser = new User(req.email(), req.password(), req.name());
            repo.save(newUser);

            return new SignupResponse(newUser.getId(), newUser.getEmail(), newUser.getName());
        }

        /**
         *  Authenticates a user by email and password
         *  @param req the login credentials
         *  @return the authenticated user's data
         *  @throws ResponseStatusException if email or password is invalid (CODE 401)
         */
        public LoginResponse login (LoginRequest req)
        {
            // TODO - Add token JWT logic in FUTURE

            /* Same message for email not found and wrong password to avoid leaking whether an email is registered */
            User user = repo.findByEmail(req.email())
                    .filter(userDB -> userDB.getPassword().equals(req.password()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid"));

            return new LoginResponse(user.getId(), user.getEmail(), user.getName());
        }

        // TODO ADD COMMENTS & TEST
        public UpdateUserResponse update (UpdateUserRequest req)
        {
            // TODO - Add token JWT logic in FUTURE

            User user = repo.findById(req.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            user.setName(req.name());
            user.setEmail(req.email());
            user.setPassword(req.password());

            repo.save(user);

            return new UpdateUserResponse(user.getId(), user.getEmail(), user.getName(), user.isActive());
        }
    }