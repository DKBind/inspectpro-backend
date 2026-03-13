package com.inspectpro.status;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspectpro.common.entities.Status;

public interface StatusJPARepo extends JpaRepository<Status, Long> {

    Status findByNameAndType(String name, String type);

    java.util.List<Status> findByType(String type);

}
