package com.lumen.LumenWorkshopBackend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.lumen.LumenWorkshopBackend.dto.Contact;

@Repository
public interface ContactRepository extends CassandraRepository<Contact, String> {
    Optional<List<Contact>> findByMobileNo(String mobileNo);
}

