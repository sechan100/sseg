package io.sseg.boundedcontext.user.account.repository;

import io.sseg.boundedcontext.user.account.model.dto.AwaitingEmailVerifyingRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface AwaitingEmailVerifyingFormRepository extends CrudRepository<AwaitingEmailVerifyingRedisEntity, String> {

}
