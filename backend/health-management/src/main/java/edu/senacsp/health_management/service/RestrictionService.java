package edu.senacsp.health_management.service;

import edu.senacsp.health_management.dto.request.restriction.CreateRestrictionRequest;
import edu.senacsp.health_management.dto.request.restriction.UpdateRestrictionRequest;
import edu.senacsp.health_management.dto.response.restriction.RestrictionResponse;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Restriction;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import edu.senacsp.health_management.repository.RestrictionRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class RestrictionService {

    private final RestrictionRepository repo;
    private final ProfileRepository profileRepo;

    private static final int TITLE_MIN = 3;
    private static final int TITLE_MAX = 50;
    private static final int NOTE_MAX = 1000;

    // Letters, digits, spaces and common punctuation
    private static final Pattern TEXT_PATTERN =
            Pattern.compile("^[\\p{L}\\p{Nd}][\\p{L}\\p{Nd} .,;:!?()'/\\-]*$");

    public RestrictionService(RestrictionRepository repo, ProfileRepository profileRepo) {
        this.repo = repo;
        this.profileRepo = profileRepo;
    }

    /**
     * Creates a new restriction for one of the authenticated user's profiles.
     *
     * <p>The title is required, trimmed and has repeated spaces collapsed. The note is optional:
     * the request already turns a missing or blank note into {@code ""}, and this method stores
     * it as {@code null} in the database. The profile is looked up by ID <em>and</em> owner,
     * so a profile that does not exist and a profile that belongs to another user produce
     * the same error, which avoids revealing which IDs exist.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param req  the profile ID, type, severity, title and optional note
     * @return a {@link RestrictionResponse} with the created restriction
     *         ({@code note} is {@code ""} when there is no note)
     * @throws ResponseStatusException if the profile ID, type, severity or title is missing or invalid,
     *                                 or the note is longer than the limit (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    public RestrictionResponse create(User user, CreateRestrictionRequest req)
    {
        // Validations

        if (req.profileId() == null || req.profileId() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        if (req.type() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is required");
        }

        if (req.severity() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Severity is required");
        }

        String title = validateTitle(req.title());
        String note = validateNote(req.note());

        // Validations 2 (DB)

        // Checks if the specified profile actually exists and belongs to this user
        Profile profile = profileRepo.findByIdAndUser(req.profileId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")); // <- Or it does not belong to this user

        // Set
        Restriction newRestriction = new Restriction(profile, req.type(), req.severity(), title, note);

        // Save and Return
        newRestriction = repo.save(newRestriction);

        return new RestrictionResponse(newRestriction);
    }

    /**
     * Lists all restrictions (active and inactive) of one of the authenticated user's profiles.
     *
     * <p>The profile is checked first, by ID <em>and</em> owner, so a profile that does not
     * exist and a profile that belongs to another user produce the same error, which avoids
     * revealing which IDs exist. Only after that are the profile's restrictions loaded.
     * A profile of this user that simply has no restrictions returns an empty list.
     *
     * @param user      the authenticated user, resolved from the JWT
     * @param profileId the ID of the profile whose restrictions are listed
     * @return the profile's restrictions (empty list if none)
     * @throws ResponseStatusException if the profile ID is missing or invalid (CODE 400)
     *                                 or the profile is not found for this user (CODE 404)
     */
    public List<RestrictionResponse> findAllByProfile(User user, Long profileId)
    {
        // Validations

        if (profileId == null || profileId <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        // Validations 2 (DB)
        // The profile must exist and belong to the authenticated user (same 404 for both cases)
        if (profileRepo.findByIdAndUser(profileId, user).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        return repo.findAllByProfileId(profileId).stream().map(RestrictionResponse::new).toList();
    }

    /**
     * Updates a restriction of the authenticated user (full replacement, like an HTTP PUT).
     *
     * <p>The restriction ID comes from the URL ({@code PUT /restriction/{id}}); the remaining
     * fields come from the request body. All body fields are required (except the note) and
     * overwrite the stored values, even when unchanged. The title is trimmed and has repeated
     * spaces collapsed; a blank note is stored as {@code null}.
     *
     * <p>The restriction is looked up by ID, profile ID <em>and</em> profile owner in a single
     * query, so a restriction that does not exist, a profile that does not exist and a
     * restriction/profile that belongs to another user all produce the same error, which
     * avoids revealing which IDs exist. The {@code profileId} only confirms the profile the
     * restriction already belongs to; it does not move the restriction to another profile.
     *
     * <p>Sending {@code active = false} archives the restriction (soft delete) and
     * {@code active = true} reactivates it.
     *
     * @param user the authenticated user, resolved from the JWT
     * @param id   the ID of the restriction to update, taken from the URL
     * @param req  the profile ID and the new type, severity, title, note and active flag
     * @return a {@link RestrictionResponse} with the updated restriction
     * @throws ResponseStatusException if the restriction ID, profile ID, type, severity, title or
     *                                 active flag is missing or invalid, or the note is longer
     *                                 than the limit (CODE 400)
     *                                 or the restriction is not found for this user (CODE 404)
     */
    public RestrictionResponse update(User user, Long id, UpdateRestrictionRequest req)
    {
        // Validations

        if (id == null || id <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restriction ID is invalid");
        }

        if (req.profileId() == null || req.profileId() <= 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile ID is invalid");
        }

        if (req.type() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is required");
        }

        if (req.severity() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Severity is required");
        }

        String title = validateTitle(req.title());
        String note = validateNote(req.note());

        if (req.active() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Active info is required");
        }

        // Validations 2 (DB)
        // One query checks the restriction, its profile and the profile owner at once.
        // Same 404 for every failure case, so no information is leaked.
        Restriction restriction = repo.findByIdAndProfileIdAndProfileUserId(id, req.profileId(), user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restriction not found"));

        // Sets
        restriction.setType(req.type());
        restriction.setSeverity(req.severity());
        restriction.setTitle(title);
        restriction.setNote(note);
        restriction.setActive(req.active());

        // Save and Return
        restriction = repo.save(restriction);

        return new RestrictionResponse(restriction);
    }

    //-----------------------------------------------------

    // VALIDATIONS
    private String validateTitle(String title)
    {
        if (title == null || title.isBlank())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }

        String t = title.trim().replaceAll("\\s+", " ");

        if (t.length() < TITLE_MIN || t.length() > TITLE_MAX)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must have between " + TITLE_MIN + " and " + TITLE_MAX + " characters");
        }

        if (!TEXT_PATTERN.matcher(t).matches())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title contains invalid characters");
        }

        return t;
    }

    private String validateNote(String note)
    {
        if (note == null || note.isBlank())
        {
            return null;
        }

        String n = note.trim();

        if (n.length() > NOTE_MAX)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note must have at most " + NOTE_MAX + " characters");
        }

        return n;
    }
}