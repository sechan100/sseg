package io.sseg.boundedcontext.user.application.repository;

import io.sseg.boundedcontext.user.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
