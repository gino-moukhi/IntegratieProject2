package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.domain.MessageDto;
import be.kdg.kandoe.repository.declaration.ChatRepository;
import be.kdg.kandoe.service.declaration.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public MessageDto saveMessage(MessageDto message) {
        return chatRepository.addMessage(message);
    }

    @Override
    public List<MessageDto> getLastMessages(int count, long sessionId) {
        return chatRepository.findLasteMessages(count, sessionId);
    }
}
