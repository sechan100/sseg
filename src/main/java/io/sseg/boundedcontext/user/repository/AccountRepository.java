package io.sseg.boundedcontext.user.repository;

import io.sseg.boundedcontext.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    boolean existsByUsernameAndProvider(String username, String provider);
    
    boolean existsByEmailAndProvider(String email, String provider);
    
    Account findByUsernameAndProvider(String username, String provider);
    
    Account findByUsername(String username);
    
    boolean existsByUsername(String username);
}
