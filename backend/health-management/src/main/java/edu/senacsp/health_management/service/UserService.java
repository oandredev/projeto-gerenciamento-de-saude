    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.user.LoginRequest;
    import edu.senacsp.health_management.dto.request.user.SignupRequest;
    import edu.senacsp.health_management.dto.request.user.UpdateUserRequest;
    import edu.senacsp.health_management.dto.response.user.AuthResponse;
    import edu.senacsp.health_management.dto.response.user.LoginResponse;
    import edu.senacsp.health_management.dto.response.user.SignupResponse;
    import edu.senacsp.health_management.dto.response.user.UpdateUserResponse;
    import edu.senacsp.health_management.entity.User;
    import edu.senacsp.health_management.repository.UserRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.nio.charset.StandardCharsets;
    import java.util.regex.Pattern;

    @Service
    public class UserService {

        private final UserRepository repo;

        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        private static final int NAME_MIN = 3;
        private static final int NAME_MAX = 255;
        private static final int EMAIL_MIN = 6;
        private static final int EMAIL_MAX = 255;
        private static final int PASSWORD_MIN = 8;
        private static final int PASSWORD_MAX_BYTES = 72;

        // Letters, digits and special characters
        private static final Pattern PASSWORD_ALLOWED = Pattern.compile("^[\\p{L}\\p{Nd}\\p{Punct}]+$");
        private static final Pattern SPECIAL = Pattern.compile("\\p{Punct}");

        private static final Pattern NAME_PATTERN = Pattern.compile("^\\p{L}+(?: \\p{L}+)*$");

        // text@text.text
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

        private static final Pattern LOWER   = Pattern.compile("\\p{Ll}");
        private static final Pattern UPPER   = Pattern.compile("\\p{Lu}");
        private static final Pattern DIGIT   = Pattern.compile("\\d");

        public UserService(UserRepository repo, PasswordEncoder passwordEncoder, JwtService jwtService) {
            this.repo = repo;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
        }

        /**
         * Registers a new user.
         *
         * <p>Name, email and password are validated first. The email is stored trimmed
         * and the password is stored only as a BCrypt hash.
         *
         * @param req the signup data (name, email and password)
         * @return a {@link SignupResponse} with the created user's data
         * @throws ResponseStatusException if any field is invalid (CODE 400)
         *                                 or the email is already in use (CODE 409)
         */
        public SignupResponse signup (SignupRequest req) {

            // Validations
            String name = validateName(req.name());
            String email = validateEmail(req.email());
            validatePassword(req.password());

            if (repo.existsByEmail(email))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }

            // Sets
            String passwordHash = passwordEncoder.encode(req.password());
            User newUser = new User(email, passwordHash, name);

            newUser = repo.save(newUser);

            return new SignupResponse(newUser.getId(), newUser.getEmail(), newUser.getName(), newUser.getModifiedAt(), newUser.getCreatedAt());
        }

        /**
         * Authenticates a user by email and password and issues a JWT.
         *
         * <p>When {@code remember} is {@code true}, the token gets a longer expiration ("remember me").
         * The same error is returned for an unknown email and a wrong password, so the
         * API does not reveal which emails are registered.
         *
         * @param req the login credentials; {@code remember} is required
         * @return an {@link AuthResponse} with the user's data and the signed token
         * @throws ResponseStatusException if email or password is missing or invalid (CODE 401)
         *                                 or {@code remember} is missing (CODE 400)
         */
        public AuthResponse login (LoginRequest req)
        {
            if (req.email() == null || req.password() == null)
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid");
            }

            if (req.remember() == null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Remember is required");
            }

            String email = req.email().trim();

            /* Same message for email not found and wrong password to avoid leaking whether an email is registered */
            User user = repo.findByEmail(email)
                    .filter(userDB -> passwordEncoder.matches(req.password(), userDB.getPassword()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password are invalid"));

            String token = jwtService.generateToken(user, req.remember());

            return new AuthResponse(new LoginResponse(user.getId(), user.getEmail(), user.getName(), user.getModifiedAt(), user.getCreatedAt()), token);
        }

        /**
         * <p>All fields are required and overwrite the stored values, even when unchanged.
         * The password is validated and hashed again with BCrypt. The user being updated is
         * the one identified by the JWT, never one taken from the request body.
         *
         * @param user the authenticated user, resolved from the JWT
         * @param req  the new name, email and password
         * @return an {@link UpdateUserResponse} with the updated user's data
         * @throws ResponseStatusException if any field is invalid (CODE 400)
         *                                 or the email belongs to another user (CODE 409)
         */
        public UpdateUserResponse update (User user, UpdateUserRequest req)
        {
            // Validations
            String name = validateName(req.name());
            String email = validateEmail(req.email());
            validatePassword(req.password());

            if (repo.existsByEmailAndIdNot(email, user.getId()))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use"); // Other user
            }

            // Sets
            user.setName(name);
            user.setEmail(email);
            String passwordHash = passwordEncoder.encode(req.password());
            user.setPassword(passwordHash);

            // Save and Return
            user = repo.save(user);
            return new UpdateUserResponse(user.getId(), user.getEmail(), user.getName(), user.isActive(), user.getModifiedAt(), user.getCreatedAt());
        }

        //-------------------------------------------------------------------------------------------------------------

        // VALIDATIONS
        private String validateName(String name)
        {
            if (name == null || name.isBlank())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
            }

            String n = name.trim();

            if (n.length() < NAME_MIN || n.length() > NAME_MAX)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must have between " + NAME_MIN + " and " + NAME_MAX + " characters");
            }

            if (!NAME_PATTERN.matcher(n).matches())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must contain only letters");
            }

            return n;
        }

        private String validateEmail(String email)
        {
            if (email == null || email.isBlank())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            }

            String e = email.trim();

            if (e.length() < EMAIL_MIN || e.length() > EMAIL_MAX)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must have between " + EMAIL_MIN + " and " + EMAIL_MAX + " characters");
            }

            if (!EMAIL_PATTERN.matcher(e).matches())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format is invalid");
            }

            return e;
        }

        private void validatePassword(String password)
        {
            if (password == null || password.isBlank())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
            }

            if (password.length() < PASSWORD_MIN)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must have at least " + PASSWORD_MIN + " characters");
            }

            if (!PASSWORD_ALLOWED.matcher(password).matches())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password contains invalid characters (emojis and spaces are not allowed)");
            }

            if (password.getBytes(StandardCharsets.UTF_8).length > PASSWORD_MAX_BYTES)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is too long");
            }

            if (!LOWER.matcher(password).find()
                    || !UPPER.matcher(password).find()
                    || !DIGIT.matcher(password).find()
                    || !SPECIAL.matcher(password).find())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain lowercase, uppercase, number and special character");
            }
        }
    }