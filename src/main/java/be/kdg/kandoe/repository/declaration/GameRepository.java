package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.Vote;

public interface GameRepository {

    Vote addVote(Vote vote);
}