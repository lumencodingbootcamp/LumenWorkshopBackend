package com.lumen.LumenWorkshopBackend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lumen.LumenWorkshopBackend.dto.Contact;
import com.lumen.LumenWorkshopBackend.dto.Message;
import com.lumen.LumenWorkshopBackend.service.CassandraService;

@RestController
@RequestMapping("/chat")
public class LumenWorkshopController {

    @Autowired
    private CassandraService cassandraService;

    // Endpoint to get contact details by mobileNo and contactMobileNo
    @RequestMapping(value = "/getContact",method = RequestMethod.GET)
    public ResponseEntity<List<Contact>> getContact(
            @RequestParam("mobileNo") String mobileNo) {
        List<Contact> contacts = cassandraService.listContacts(mobileNo);

        if (contacts.isEmpty()) {
            return ResponseEntity.ok(contacts); // Return 404 if no contacts are found
        }

        // Return the list of contacts with status 200 OK if found
        return ResponseEntity.ok(contacts);
    }

    // Endpoint to add a new contact
    @PostMapping(value = "/addContact")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        try {
            Contact savedContact = cassandraService.createContact(contact.getMobileNo(), contact.getContactMobileNo(), contact.getConversationKey());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
        } catch (Exception e) {
        	e.printStackTrace();
        	if(e.getMessage()=="User not registered in the system") {
        		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        	}
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping(value = "/getMessages")
    public ResponseEntity<List<Message>> getMessages(@RequestParam String conversationKey) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(cassandraService.getMessagesByConversationKey(conversationKey));
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

