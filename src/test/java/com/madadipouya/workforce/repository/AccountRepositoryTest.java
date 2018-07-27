package com.madadipouya.workforce.repository;

import com.madadipouya.workforce.model.Account;
import com.madadipouya.workforce.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link AccountRepository}
 */

@TestPropertySource(locations = {"classpath:test.properties"})
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getAccountByName() {
        Account savedAccount = entityManager.persistAndFlush(new Account("admin1", "password1"));
        Account account = accountRepository.findByUsername(savedAccount.getUsername());
        assertEquals(savedAccount.getId(), account.getId());
        assertEquals(savedAccount.getUsername(), account.getUsername());
        assertEquals(savedAccount.getPassword(), account.getPassword());
    }
}
