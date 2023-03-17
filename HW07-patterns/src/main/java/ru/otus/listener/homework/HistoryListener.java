package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message msgCopy = new Message(msg);
        messageMap.put(msgCopy.getId(), msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageMap.get(id));
    }
}
