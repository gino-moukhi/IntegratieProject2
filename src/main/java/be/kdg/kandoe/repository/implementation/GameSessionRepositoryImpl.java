package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.repository.declaration.GameSessionRepository;
import be.kdg.kandoe.repository.declaration.GameSessionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class GameSessionRepositoryImpl implements GameSessionRepositoryCustom{

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<GameSession> findGameSessionsOfUserWithUsername(String username) {

        /*
        SELECT *
        FROM GAME_SESSION gs
        JOIN USER_GAME_SESSION_INFO ugsi ON gs.game_session_id = ugsi.game_session_game_session_id
        JOIN USERS u ON ugsi.user_user_id = u.user_id
        WHERE u.username = 'USERNAME'
        */

        //TODO FIX
//        TypedQuery<GameSession[]> query = em.createQuery("SELECT gs.GAME_SESSION_ID\n" +
//                "        FROM GAME_SESSION gs\n" +
//                "        JOIN USER_GAME_SESSION_INFO ugsi ON gs.game_session_id = ugsi.game_session_game_session_id\n" +
//                "        JOIN USERS u ON ugsi.user_user_id = u.user_id\n" +
//                "        WHERE u.username = " + username, GameSession[].class);


//        Query query = em.createQuery("SELECT *\n" +
//                "        FROM GAME_SESSION gs\n" +
//                "        JOIN USER_GAME_SESSION_INFO ugsi ON gs.game_session_id = ugsi.game_session_game_session_id\n" +
//                "        JOIN USERS u ON ugsi.user_user_id = u.user_id\n" +
//                "        WHERE u.username = " + username);



        return null;
    }

}
