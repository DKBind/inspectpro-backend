package com.inspectpro.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspectpro.common.entities.Users;

public interface UserJPARepo extends JpaRepository<Users, UUID> {

}
