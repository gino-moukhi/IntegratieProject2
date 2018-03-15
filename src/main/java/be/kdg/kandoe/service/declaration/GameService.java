package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.Vote;
import be.kdg.kandoe.domain.user.User;

public interface GameService {
    public void startSession(long sessionId);

    public Vote MakeVote(long sessionId, long cardId, User user);
}