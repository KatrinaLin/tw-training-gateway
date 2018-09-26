package com.example.gateway.config;

import com.example.gateway.security.CustomBasicAuthenticationFilter;
import com.example.gateway.security.CustomBasicAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomBasicAuthenticationProvider customBasicAuthenticationProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customBasicAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/products/**").permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable()
                .addFilterBefore(new CustomBasicAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }


    @Bean
    public CustomBasicAuthenticationProvider customBasicAuthenticationProvider() {
        return new CustomBasicAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}