package com.madadipouya.workforce.repository;

import com.madadipouya.workforce.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Account} to support basic auth implementation
 * and to authenticate a username and password
 * against database table
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}
