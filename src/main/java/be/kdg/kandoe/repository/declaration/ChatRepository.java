package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.Message;

import java.util.List;

public interface ChatRepository {
    List<Message> findLasteMessages(int count, long sessionId);

    Message addMessage(Message message);
}
