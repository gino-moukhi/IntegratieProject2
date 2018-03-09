package be.kdg.kandoe.domain;

/**
 * This enum represents the role of a user in a game session
 * Moderator --> The original creator of the game session (highest access level, and also marks the organisator as not participating in the game session)
 * Participant --> A user that participates in the game session but has no extra rights
 * ModeratorParticipant --> The original creator of the game session (highest access level, and also marks the organisator as participating in the game session)
 * SubModerator --> A user that participates in the game session and that has been granted extra rights (second highest access level)
 */
public enum GameSessionRole {
    Moderator,
    Participant,
    ModeratorParticipant,
    SubModerator
}

