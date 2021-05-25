package br.com.jusnexo.service.impl;

import br.com.jusnexo.domain.Chat;
import br.com.jusnexo.repository.ChatRepository;
import br.com.jusnexo.service.ChatService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Chat}.
 */
@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat save(Chat chat) {
        log.debug("Request to save Chat : {}", chat);
        return chatRepository.save(chat);
    }

    @Override
    public Optional<Chat> partialUpdate(Chat chat) {
        log.debug("Request to partially update Chat : {}", chat);

        return chatRepository
            .findById(chat.getId())
            .map(
                existingChat -> {
                    if (chat.getCreatedAt() != null) {
                        existingChat.setCreatedAt(chat.getCreatedAt());
                    }

                    return existingChat;
                }
            )
            .map(chatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Chat> findAll(Pageable pageable) {
        log.debug("Request to get all Chats");
        return chatRepository.findAll(pageable);
    }

    public Page<Chat> findAllWithEagerRelationships(Pageable pageable) {
        return chatRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chat> findOne(Long id) {
        log.debug("Request to get Chat : {}", id);
        return chatRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chat : {}", id);
        chatRepository.deleteById(id);
    }
}
