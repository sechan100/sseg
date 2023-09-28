package io.sseg.boundedContext.user.repository;

import io.sseg.boundedContext.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    Account findByUsername(String username);
}
