package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.Message;

import java.util.List;

public interface ChatService {
    Message saveMessage(Message message);

    List<Message> getLastMessages(int count, long sessionId);
}
