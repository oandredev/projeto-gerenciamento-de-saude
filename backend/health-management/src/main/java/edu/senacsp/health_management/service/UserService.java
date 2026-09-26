    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.user.LoginRequest;
    import edu.senacsp.health_management.dto.request.user.SignupRequest;
    import edu.senacsp.health_management.dto.response.user.LoginResponse;
    import edu.senacsp.health_management.dto.response.user.SignupResponse;
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

            User newUser = new User(req.name(), req.email(), req.password());
            repo.save(newUser);

            return new SignupResponse(newUser.getId(), newUser.getName(), newUser.getEmail());
        }

        /**
         *  Authenticates a user by email and password
         *  @param req the login credentials
         *  @return the authenticated user's data
         *  @throws ResponseStatusException if email or password is invalid (CODE 401)
         */
        public LoginResponse login (LoginRequest req)
        {
            /* Same message for email not found and wrong password to avoid leaking whether an email is registered */
            User user = repo.findByEmail(req.email())
                    .filter(userDB -> userDB.getPassword().equals(req.password()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid"));

            // TODO - Add token JWT logic in FUTURE

            return new LoginResponse(user.getEmail(), user.getName());
        }
    }