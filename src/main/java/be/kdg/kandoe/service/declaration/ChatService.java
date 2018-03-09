package be.kdg.kandoe.service.declaration;

import be.kdg.kandoe.domain.MessageDto;

import java.util.List;

public interface ChatService {
    MessageDto saveMessage(MessageDto message);

    List<MessageDto> getLastMessages(int count, long sessionId);
}
