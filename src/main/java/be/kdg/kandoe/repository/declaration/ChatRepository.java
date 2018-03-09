package be.kdg.kandoe.repository.declaration;

import be.kdg.kandoe.domain.MessageDto;

import java.util.List;

public interface ChatRepository {
    List<MessageDto> findLasteMessages(int count, long sessionId);

    MessageDto addMessage(MessageDto message);
}
