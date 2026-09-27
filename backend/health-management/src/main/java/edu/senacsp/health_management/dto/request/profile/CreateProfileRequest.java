package edu.senacsp.health_management.dto.request.profile;

/**
 * Request payload for creating a new Restriction.
 *
 * <p>Example JSON:
 * <pre>{@code
 * {
 *     "profileId": 1,
 *     "type": "MEDICATION",
 *     "severity": "SEVERE",
 *     "title": "ALERGIA A DEXAMETASONA",
 *     "note": "Não tomar em hipótese nenhuma. Em caso de emergência ir ao pronto-socorro o mais rápido possível.",
 *     "active": true
 * }
 * }</pre>
 */
public record CreateProfileRequest(
        Long userId,
        String name,
        Long avatarId
) {}