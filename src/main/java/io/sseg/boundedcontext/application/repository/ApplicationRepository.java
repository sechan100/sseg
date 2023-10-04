package io.sseg.boundedcontext.application.repository;

import io.sseg.boundedcontext.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByOwnerId(Long ownerId);
    
    Application findByAppId(String appId);
}
