    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
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

    @Service
    public class ProfileService {

        private final ProfileRepository repo;
        private final UserRepository userRepo;

        public ProfileService(ProfileRepository repo, UserRepository userRepo) {
            this.repo = repo;
            this.userRepo = userRepo;
        }

        /**
         * @param req the
         * @return a @{@link CreateProfileResponse} with new Profile data
         * @throws ResponseStatusException if email already in use (CODE 409)
         */
        public CreateProfileResponse create (CreateProfileRequest req) {

            // Verify if user exists
            User user = userRepo.findById(req.userId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // TODO Change the message later to avoid leaking information.

            // Verify if the user already has a profile with the same name
            if(repo.existsByNameAndUser(req.name(), user))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This profile name is already being used by this same user");
            }

            Profile newProfile = new Profile(req.name(), user, req.avatarId());

            repo.save(newProfile);

            return new CreateProfileResponse(new ProfileItem(newProfile));
        }

        /**
         * @param userId the id of the user whose profiles will be listed
         * @return a {@link ListProfileResponse} containing the user's {@link ProfileItem} list
         * @throws ResponseStatusException if the user is not found (404)
         */
        public ListProfileResponse findAllByUser(Long userId)
        {
            // Verify if user exists
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // TODO Change the message later to avoid leaking information.

            List<ProfileItem> profileItemsList = repo.findAllByUser(user);

            return new ListProfileResponse(profileItemsList);
        }

        /**
         * @param profileItem the profile data to update, including the profile id and the owner's user id
         * @return an {@link UpdateProfileResponse} with the updated profile data
         * @throws ResponseStatusException if the profile is not found (404), or if the informed
         *         user is not the owner of this profile (403)
         */
        public UpdateProfileResponse update(ProfileItem profileItem)
        {
            Profile profile = repo.findById(profileItem.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")); // TODO Change the message later to avoid leaking information.

            if (!profile.getUser().getId().equals(profileItem.userId()))
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This profile does not belong to the informed user"); // TODO Change the message later to avoid leaking information.
            }

            profile.setName(profileItem.name());
            profile.setAvatarId(profileItem.avatarId());
            profile.setActive(profileItem.active());

            repo.save(profile);

            return new UpdateProfileResponse(profileItem);
        }
    }