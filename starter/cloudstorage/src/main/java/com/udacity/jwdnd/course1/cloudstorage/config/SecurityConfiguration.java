package com.udacity.jwdnd.course1.cloudstorage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    @Autowired
    public SecurityConfiguration(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**","/h2/**", "/console/**","/h2-console/**")
            .permitAll()
            .anyRequest().authenticated();


//        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();//Needed for /h2 console or else white label error
        http.headers().frameOptions().disable();

        http.formLogin()
            .loginPage("/login")
            .permitAll();


        http.formLogin()
            .defaultSuccessUrl("/home", true);

        http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login").permitAll();
    }


}