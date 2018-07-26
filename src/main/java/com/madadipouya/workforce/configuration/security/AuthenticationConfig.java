package com.madadipouya.workforce.configuration.security;

import com.madadipouya.workforce.model.Account;
import com.madadipouya.workforce.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

    private AccountRepository accountRepository;

    @Autowired
    public AuthenticationConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Account account = accountRepository.findByUsername(username);
            if (account != null) {
                return new User(account.getUsername(), "{noop}" + account.getPassword(), true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("could not find the user '"
                        + username + "'");
            }
        };
    }
}
