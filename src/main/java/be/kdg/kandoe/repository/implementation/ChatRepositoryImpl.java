package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.MessageDto;
import be.kdg.kandoe.repository.declaration.ChatRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MessageDto> findLasteMessages(int count, long sessionId) {
        TypedQuery<MessageDto> q = em.createQuery("SELECT * from message m where m.session.sessionId = :sessionId order by m.dateTime desc ", MessageDto.class).setMaxResults(count);
        q.setParameter("sessionId", sessionId);
        List<MessageDto> messages =  q.getResultList();
        messages.sort((m1,m2)->m1.getDateTime().compareTo(m2.getDateTime()));
        return messages;
    }

    @Override
    public MessageDto addMessage(MessageDto message) {
        em.persist(message);
        return message;
    }
}
