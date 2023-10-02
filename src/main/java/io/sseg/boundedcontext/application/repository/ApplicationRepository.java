package io.sseg.boundedcontext.application.repository;

import io.sseg.boundedcontext.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
