    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
    import edu.senacsp.health_management.dto.request.profile.UpdateProfileRequest;
    import edu.senacsp.health_management.dto.response.profile.CreateProfileResponse;
    import edu.senacsp.health_management.dto.response.profile.ListProfileResponse;
    import edu.senacsp.health_management.dto.response.profile.ProfileItem;
    import edu.senacsp.health_management.dto.response.profile.UpdateProfileResponse;
    import edu.senacsp.health_management.entity.Profile;
    import edu.senacsp.health_management.entity.User;
    import edu.senacsp.health_management.repository.ProfileRepository;
    import edu.senacsp.health_management.repository.UserRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.List;
    import java.util.regex.Pattern;

    @Service
    public class ProfileService {

        private final ProfileRepository repo;
        private final UserRepository userRepo;

        private static final int NAME_MIN = 3;
        private static final int NAME_MAX = 255;

        private static final Pattern NAME_PATTERN = Pattern.compile("^\\p{L}+(?: \\p{L}+)*$");

        public ProfileService(ProfileRepository repo, UserRepository userRepo) {
            this.repo = repo;
            this.userRepo = userRepo;
        }

        /**
         * Creates a new profile for the authenticated user.
         *
         * <p>The name is trimmed and has repeated spaces collapsed before being validated
         * and stored. A user cannot have two profiles with the same name.
         *
         * @param user the authenticated user, resolved from the JWT (owner of the new profile)
         * @param req  the profile data (name and avatar ID)
         * @return a {@link CreateProfileResponse} with the created profile
         * @throws ResponseStatusException if the name or the avatar ID is invalid (CODE 400)
         *                                 or the user already has a profile with this name (CODE 409)
         */
        public CreateProfileResponse create (User user, CreateProfileRequest req) {

            // Validations
            String name = validateName(req.name());

            if (req.avatarId() < 0)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar ID is invalid");
            }

            // Verify if the user already has a profile with the same name
            if(repo.existsByNameAndUser(name, user))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This profile name is already being used by this same user");
            }

            // Save and Return
            Profile newProfile = new Profile(name, user, req.avatarId());
            newProfile = repo.save(newProfile);

            return new CreateProfileResponse(new ProfileItem(newProfile));
        }

        /**
         * Lists the profiles that belong to the authenticated user.
         *
         * @param user the authenticated user, resolved from the JWT
         * @return a {@link ListProfileResponse} with the user's profiles (empty list if none)
         */
        public ListProfileResponse findAllByUser(User user)
        {
            List<ProfileItem> profileItemsList = repo.findAllByUser(user);

            return new ListProfileResponse(profileItemsList);
        }

        /**
         * Updates a profile of the authenticated user (full replacement, like an HTTP PUT).
         *
         * <p>All fields are required and overwrite the stored values, even when unchanged.
         * The profile is looked up by ID <em>and</em> owner, so a profile that does not exist
         * and a profile that belongs to another user produce the same error, which avoids
         * revealing which IDs exist.
         *
         * @param user the authenticated user, resolved from the JWT
         * @param req  the profile ID and its new name, avatar ID and active flag
         * @return an {@link UpdateProfileResponse} with the updated profile
         * @throws ResponseStatusException if any field is missing or invalid (CODE 400)
         *                                 or the profile is not found for this user (CODE 404)
         */
        public UpdateProfileResponse update(User user, UpdateProfileRequest req)
        {
            // Validations
            if (req.id() == null || req.id() < 0)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is invalid");
            }

            if (req.avatarId() == null || req.avatarId() < 0)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar ID is invalid");
            }

            if (req.active() == null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Active info is required");
            }

            Profile profile = repo.findByIdAndUserId(req.id(), user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

            String name = validateName(req.name());

            // Sets
            profile.setName(name);
            profile.setAvatarId(req.avatarId());
            profile.setActive(req.active());

            // Save and Return
            profile = repo.save(profile);
            ProfileItem profileItem = new ProfileItem(profile);
            return new UpdateProfileResponse(profileItem);
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
    }