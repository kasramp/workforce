package com.madadipouya.workforce.configuration.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/v1/employees/**")
                .fullyAuthenticated()
                .and()
                .httpBasic()
                .and()
                .anonymous().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().csrf().disable().headers().frameOptions().disable(); // to enable h2 console TODO REMOVE IT
    }
}
