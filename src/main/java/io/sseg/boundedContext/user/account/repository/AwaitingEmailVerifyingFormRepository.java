package io.sseg.boundedContext.user.account.repository;

import io.sseg.boundedContext.user.account.model.dto.AwaitingEmailVerifyingRedisEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AwaitingEmailVerifyingFormRepository extends CrudRepository<AwaitingEmailVerifyingRedisEntity, String> {

}
