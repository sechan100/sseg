package io.sseg.boundedContext.user.account.repository;

import io.sseg.boundedContext.user.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    boolean existsByUsernameAndProvider(String username, String provider);
    
    boolean existsByEmailAndProvider(String email, String provider);
    
    Account findByUsernameAndProvider(String username, String provider);
    
    Account findByUsername(String username);
    
    boolean existsByUsername(String username);
}
