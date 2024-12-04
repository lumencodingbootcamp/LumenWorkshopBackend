package com.lumen.LumenWorkshopBackend.repo;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.lumen.LumenWorkshopBackend.dto.User;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {
    Optional<User> findByMobileNo(String mobileNo);
}

