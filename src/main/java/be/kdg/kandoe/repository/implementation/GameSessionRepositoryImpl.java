package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.repository.declaration.GameSessionRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GameSessionRepositoryImpl implements GameSessionRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<GameSession> findGameSessionsOfUserWithUsername(String username) {
        try{
            Query q = em.createQuery("SELECT gs from " +
                    "GameSession gs " +
                    "JOIN gs.userGameSessionInfos ugsi " +
                    "JOIN ugsi.user u " +
                    "WHERE u.username = :username ").setParameter("username", username);
            return q.getResultList();
        }catch (Exception e){
            String error = "Something went wrong while accessing the database!";
            return new ArrayList<>();
        }
    }
}
