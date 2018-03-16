package be.kdg.kandoe.repository.implementation;

import be.kdg.kandoe.domain.Message;
import be.kdg.kandoe.repository.declaration.ChatRepository;
import be.kdg.kandoe.repository.jpa.MessageJpa;
import be.kdg.kandoe.repository.jpa.converter.JpaConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Message> findLasteMessages(int count, long sessionId) {
        TypedQuery<MessageJpa> q = em.createQuery("SELECT m from message m where m.session.sessionId = :sessionId order by m.dateTime desc ", MessageJpa.class).setMaxResults(count);
        q.setParameter("sessionId", sessionId);
        List<Message> messages =  q.getResultList().stream().map(m->JpaConverter.toMessage(m)).collect(Collectors.toList());
        messages.sort((m1,m2)->m1.getDateTime().compareTo(m2.getDateTime()));
        return messages;
    }

    @Override
    public Message addMessage(Message message) {
        em.persist(JpaConverter.toMessageJpa(message));
        return message;
    }
}
