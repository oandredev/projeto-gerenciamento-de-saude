package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.request.restriction.CreateRestrictionRequest;
import edu.senacsp.health_management.dto.request.restriction.UpdateRestrictionRequest;
import edu.senacsp.health_management.dto.response.restriction.CreateRestrictionResponse;
import edu.senacsp.health_management.dto.response.restriction.ListRestrictionResponse;
import edu.senacsp.health_management.dto.response.restriction.RestrictionItem;
import edu.senacsp.health_management.dto.response.restriction.UpdateRestrictionResponse;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Restriction;
import edu.senacsp.health_management.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import edu.senacsp.health_management.repository.RestrictionRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RestrictionService {

    private final RestrictionRepository repo;
    private final ProfileRepository profileRepo;

    private final int TITLE_MIN = 3;
    private final int TITLE_MAX = 50;
    private final int NOTE_MAX = 500;

    public RestrictionService(RestrictionRepository repo, ProfileRepository profileRepo) {
        this.repo = repo;
        this.profileRepo = profileRepo;
    }

    // TODO ADD COMMENTS
    public CreateRestrictionResponse create(CreateRestrictionRequest req)
    {
        // Verifies if profile informed really exists
        Profile profile = profileRepo.findById(req.profileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")); // TODO Change the message later to avoid leaking information.

        // Validation (throw an exception if it fails)
        validateData(req.title(), req.note());

        Restriction newRestriction = new Restriction(profile, req.type(), req.severity(), req.title(), req.note(), req.active());

        repo.saveAndFlush(newRestriction);

        return new CreateRestrictionResponse(
                newRestriction.getId(),
                newRestriction.getProfile().getId(),
                newRestriction.getType(),
                newRestriction.getSeverity(),
                newRestriction.getTitle(),
                newRestriction.getNote(),
                newRestriction.isActive(),
                newRestriction.getModifiedAt(),
                newRestriction.getCreatedAt()
        );
    }

    // TODO ADD COMMENTS
    public ListRestrictionResponse findAllByProfile(Long profileId)
    {
        // Verify if profile exists
        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")); // TODO Change the message later to avoid leaking information.

        List<RestrictionItem> restrictionItemsList = repo.findAllByProfile(profile);

        return new ListRestrictionResponse(restrictionItemsList);
    }

    // TODO ADD COMMENTS
    public UpdateRestrictionResponse updateRestriction(UpdateRestrictionRequest req)
    {
        // Verify if profile exists
        Profile profile = profileRepo.findById(req.profileId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")); // TODO Change the message later to avoid leaking information.

        // The restriction may exist, but if it belongs to a different profile,
        // findByIdAndProfile returns empty — treated as NOT_FOUND (not FORBIDDEN)
        // to avoid leaking whether the resource exists to a non-owner.
        Restriction restriction = repo.findByIdAndProfile(req.id(), profile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restriction not found")); // TODO Change the message later to avoid leaking information.

        // Validation (throw an exception if it fails)
        validateData(req.title(), req.note());

        // Change current data
        restriction.setType(req.type());
        restriction.setSeverity(req.severity());
        restriction.setTitle(req.title());
        restriction.setNote(req.note());
        restriction.setActive(req.active());

        repo.saveAndFlush(restriction);

        return new UpdateRestrictionResponse(
                restriction.getId(),
                restriction.getProfile().getId(),
                restriction.getType(),
                restriction.getSeverity(),
                restriction.getTitle(),
                restriction.getNote(),
                restriction.isActive(),
                restriction.getModifiedAt(),
                restriction.getCreatedAt()
        );
    }

    //-----------------------------------------------------

    private void validateData(String title, String note)
    {
        int titleLength = title != null ? title.length() : 0;
        int noteLength = note != null ? note.length() : 0;

        // Validates the title length
        if (titleLength < TITLE_MIN || titleLength > TITLE_MAX)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The title must be between %d and %d characters long. The current title has %d characters"
                            .formatted(TITLE_MIN, TITLE_MAX, titleLength));
        }

        // Validates the note length
        if (noteLength > NOTE_MAX)
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The title can be in the max %d characters long. The current title has %d characters"
                            .formatted(NOTE_MAX, noteLength));
        }
    }
}