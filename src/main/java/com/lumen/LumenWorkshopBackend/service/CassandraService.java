package com.lumen.LumenWorkshopBackend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lumen.LumenWorkshopBackend.dto.Contact;
import com.lumen.LumenWorkshopBackend.dto.Message;
import com.lumen.LumenWorkshopBackend.dto.User;
import com.lumen.LumenWorkshopBackend.repo.ContactRepository;
import com.lumen.LumenWorkshopBackend.repo.MessageRepository;
import com.lumen.LumenWorkshopBackend.repo.UserRepository;

@Service
public class CassandraService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private MessageRepository messageRepository;

    // 1. Insert User if Mobile No does not exist
    public String insertUser(String mobileNo) {
        Optional<User> existingUser = userRepository.findByMobileNo(mobileNo);
        if (existingUser.isPresent()) {
            return existingUser.get().getMobileNo();
        }
        
        User newUser = new User();
        newUser.setMobileNo(mobileNo);
        userRepository.save(newUser);
        return mobileNo;
    }

    // 2. Create Contact if it doesn't exist already (Generate Unique Conversation Key)
    public Contact createContact(String mobileNo, String contactMobileNo, String inputConversationKey) throws Exception {
        Optional<List<Contact>> existingContact = contactRepository.findByMobileNo(mobileNo);
        if (existingContact.isPresent() && existingContact.get().stream().anyMatch(contact->contact.getContactMobileNo().equals(contactMobileNo))) {
            return null;
        }
        else {
        	Optional<User> user = userRepository.findByMobileNo(contactMobileNo);
        	if(!user.isPresent()) {
        		throw new Exception("User not registered in the system");
        	}
        }
        String conversationKey = inputConversationKey!=null ? inputConversationKey : UUID.randomUUID().toString();
        Contact newContact = new Contact();
        newContact.setMobileNo(mobileNo);
        newContact.setContactMobileNo(contactMobileNo);
        newContact.setConversationKey(conversationKey);
        contactRepository.save(newContact);
        return newContact;
    }

    // 3. List all Contacts for a User
    public List<Contact> listContacts(String mobileNo) {
    	return contactRepository.findByMobileNo(mobileNo)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    // 4. Add Message to Messages Table
    public String addMessage(String conversationKey, String message, String sender, String receiver, String messageId, Timestamp timestamp) {
        Message newMessage = new Message();
        newMessage.setMessageId(messageId);
        newMessage.setConversationKey(conversationKey);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setMessage(message);
        newMessage.setTimestamp(timestamp);
        messageRepository.save(newMessage);
        return "Message added successfully!";
    }

    // 5. Retrieve Messages for a Particular Conversation Key
    public List<Message> getMessagesByConversationKey(String conversationKey) {
        return messageRepository.findByConversationKey(conversationKey);
    }
}

