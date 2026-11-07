    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.user.LoginRequest;
    import edu.senacsp.health_management.dto.request.user.SignupRequest;
    import edu.senacsp.health_management.dto.request.user.UpdatePasswordRequest;
    import edu.senacsp.health_management.dto.request.user.UpdateUserRequest;
    import edu.senacsp.health_management.dto.response.user.AuthResponse;
    import edu.senacsp.health_management.dto.response.user.UserResponse;
    import edu.senacsp.health_management.dto.response.profile.ProfileResponse;

    import edu.senacsp.health_management.entity.User;

    import edu.senacsp.health_management.repository.UserRepository;

    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.security.crypto.password.PasswordEncoder;

    import java.util.List;
    import java.util.regex.Pattern;

    import java.nio.charset.StandardCharsets;

    @Service
    public class UserService
    {

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

        private static final Pattern LOWER = Pattern.compile("\\p{Ll}");
        private static final Pattern UPPER = Pattern.compile("\\p{Lu}");
        private static final Pattern DIGIT = Pattern.compile("\\d");

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
         * @return a {@link UserResponse} with the created user's data
         *         ({@code profiles} is always empty for a new user)
         * @throws ResponseStatusException if any field is invalid (CODE 400)
         *                                 or the email is already in use (CODE 409)
         */
        public UserResponse signup (SignupRequest req) {

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

            return new UserResponse(newUser.getId(), newUser.getEmail(), newUser.getName(), List.of(), newUser.isActive(), newUser.getModifiedAt(), newUser.getCreatedAt());
        }

        /**
         * Authenticates a user by email and password and issues a JWT.
         *
         * <p>When {@code remember} is {@code true}, the token gets a longer expiration ("remember me").
         * The same error is returned for an unknown email and a wrong password, so the
         * API does not reveal which emails are registered.
         *
         * <p>The returned user includes all of their profiles, active and inactive.
         * Clients must use each profile's {@code active} flag to decide how to display it.
         *
         * @param req the login credentials; {@code remember} is required
         * @return an {@link AuthResponse} with the user's data (including profiles) and the signed token
         * @throws ResponseStatusException if email or password is missing or invalid,
         *                                 or the account is archived (CODE 401),
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

            if (!user.isActive())
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This account was archived");
            }

            String token = jwtService.generateToken(user, req.remember());

            List<ProfileResponse> profiles = user.getProfiles().stream()
                    .map(ProfileResponse::new)
                    .toList();

            return new AuthResponse(new UserResponse(user.getId(), user.getEmail(), user.getName(), profiles, user.isActive(), user.getModifiedAt(), user.getCreatedAt()), token);
        }

        /**
         * Updates the authenticated user's name and email (full replacement, like an HTTP PUT).
         *
         * <p>Name and email are required and overwrite the stored values, even when unchanged.
         * The name has its extra spaces collapsed and the email is trimmed before being stored.
         *
         * <p>Changing the email is a sensitive change, so it also requires the current password,
         * which must match the stored one. The comparison is case-sensitive, so changing only the
         * letter case of the email also counts as a change. The email uniqueness check only runs
         * when the email changes. The password itself is not changed here.
         *
         * <p>The user being updated is the one identified by the JWT, never one taken from the
         * request body.
         *
         * @param user the authenticated user, resolved from the JWT
         * @param req  the name, email and (when the email changes) the current password
         * @return a {@link UserResponse} with the updated user's data and all of their profiles
         * @throws ResponseStatusException if name or email is missing or invalid, or the current
         *                                 password is missing when required (CODE 400),
         *                                 the authenticated user no longer exists (CODE 401),
         *                                 the current password is incorrect (CODE 403),
         *                                 or the email belongs to another user (CODE 409)
         */
        @Transactional
        public UserResponse update(User user, UpdateUserRequest req)
        {
            String name = validateName(req.name());
            String email = validateEmail(req.email());

            User userDB = repo.findWithProfilesById(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

            boolean changeEmail = !email.equals(userDB.getEmail());

            // Sensitive change (email) requires the current password
            if (changeEmail)
            {
                if (req.currentPassword() == null || req.currentPassword().isBlank())
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
                }

                if (!passwordEncoder.matches(req.currentPassword(), userDB.getPassword()))
                {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current password is incorrect");
                }

                if (repo.existsByEmailAndIdNot(email, userDB.getId()))
                {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
            }

            userDB.setName(name);
            userDB.setEmail(email);

            userDB = repo.saveAndFlush(userDB);

            List<ProfileResponse> profiles = userDB.getProfiles().stream()
                    .map(ProfileResponse::new)
                    .toList();

            return new UserResponse(userDB.getId(), userDB.getEmail(), userDB.getName(), profiles,
                    userDB.isActive(), userDB.getModifiedAt(), userDB.getCreatedAt());
        }

        /**
         * Changes the authenticated user's password.
         *
         * <p>The current password must match the stored one. Only after that is the new password
         * validated, using the same rules as in signup, and stored as a BCrypt hash.
         * The user being updated is the one identified by the JWT, never one taken from the request body.
         *
         * @param user the authenticated user, resolved from the JWT
         * @param req  the current and the new password
         * @return a {@link UserResponse} with the user's data and all of their profiles
         * @throws ResponseStatusException if the current password is missing,
         *                                 or the new password is missing or invalid (CODE 400),
         *                                 the authenticated user no longer exists (CODE 401),
         *                                 or the current password is incorrect (CODE 403)
         */
        @Transactional
        public UserResponse updatePassword(User user, UpdatePasswordRequest req)
        {
            // Validations

            // Only checks presence, not the password rules: the rules may change over time,
            // and validating them here would reject users whose old password no longer meets them.
            // Whether it is correct is decided by passwordEncoder.matches.
            if (req.currentPassword() == null || req.currentPassword().isBlank())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
            }

            User userDB = repo.findWithProfilesById(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

            if (!passwordEncoder.matches(req.currentPassword(), userDB.getPassword()))
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current password is incorrect");
            }

            validatePassword(req.newPassword());

            userDB.setPassword(passwordEncoder.encode(req.newPassword()));

            userDB = repo.saveAndFlush(userDB);

            List<ProfileResponse> profiles = userDB.getProfiles().stream()
                    .map(ProfileResponse::new)
                    .toList();

            return new UserResponse(userDB.getId(), userDB.getEmail(), userDB.getName(), profiles,
                    userDB.isActive(), userDB.getModifiedAt(), userDB.getCreatedAt());
        }

        //-------------------------------------------------------------------------------------------------------------

        // VALIDATIONS
        private String validateName(String name)
        {
            if (name == null || name.isBlank())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
            }

            String n = name.trim().replaceAll("\\s+", " ");

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
