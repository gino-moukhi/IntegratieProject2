package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.Vote;
import be.kdg.kandoe.repository.declaration.GameRepository;
import be.kdg.kandoe.repository.jpa.VoteJpa;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class GameRepositoryImpl implements GameRepository {


    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public GameRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Vote addVote(Vote vote) {
        VoteJpa jpa = JpaConverter.toVoteJpa(vote);
        em.persist(jpa);
        return JpaConverter.toVote(jpa);
    }
}