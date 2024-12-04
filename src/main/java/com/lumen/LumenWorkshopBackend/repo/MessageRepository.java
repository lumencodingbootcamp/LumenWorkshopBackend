package com.lumen.LumenWorkshopBackend.repo;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.lumen.LumenWorkshopBackend.dto.Message;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
    List<Message> findByConversationKey(String conversationKey);
}

