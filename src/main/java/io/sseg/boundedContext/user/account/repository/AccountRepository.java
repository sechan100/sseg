package io.sseg.boundedContext.user.account.repository;

import io.sseg.boundedContext.user.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    Account findByUsername(String username);
    
    Account findByEmail(String email);
    
    Account findByUsernameAndProvider(String username, String provider);
}
