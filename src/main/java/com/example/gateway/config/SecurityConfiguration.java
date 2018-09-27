package com.example.gateway.config;

import com.example.gateway.security.CustomBasicAuthenticationFilter;
import com.example.gateway.security.CustomBasicAuthenticationProvider;
import com.example.gateway.security.CustomTokenAuthenticationFilter;
import com.example.gateway.security.CustomTokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customBasicAuthenticationProvider());
        auth.authenticationProvider(customTokenAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll().and()
//                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .addFilterBefore(new CustomBasicAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .addFilterBefore(new CustomTokenAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Bean
    public CustomBasicAuthenticationProvider customBasicAuthenticationProvider() {
        return new CustomBasicAuthenticationProvider();
    }
    
    @Bean
    public CustomTokenAuthenticationProvider customTokenAuthenticationProvider() {
        return new CustomTokenAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}