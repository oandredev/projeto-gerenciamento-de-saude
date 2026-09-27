    package edu.senacsp.health_management.service;

    import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
    import edu.senacsp.health_management.dto.response.profile.CreateProfileResponse;
    import edu.senacsp.health_management.dto.response.profile.ListProfileResponse;
    import edu.senacsp.health_management.dto.response.profile.ProfileItem;
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
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            // Verify if the user already has a profile with the same name
            if(repo.existsByNameAndUser(req.name(), user))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This profile name is already being used by this same user");
            }

            Profile newProfile = new Profile(req.name(), user, req.avatarId());

            repo.save(newProfile);

            return new CreateProfileResponse(new ProfileItem(newProfile));
        }

        // TODO COMMENTS
        public ListProfileResponse findAll(Long userId)
        {
            // Verify if user exists
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            List<ProfileItem> profileItemsList = repo.findByUser(user);

            return new ListProfileResponse(profileItemsList);
        }
    }