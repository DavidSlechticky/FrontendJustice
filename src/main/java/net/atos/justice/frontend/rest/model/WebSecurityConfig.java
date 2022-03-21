/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.atos.justice.frontend.rest.model;

import net.atos.justice.frontend.rest.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// hlavni konfigurace spring security
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/complex/**", "/latin-ext/**", "/img/**", "/js/**", "/css/**", 
                "/test", "/napoveda", "/home", "/",
                "/webjars/bootstrap/4.5.0/css/**", "/webjars/bootstrap/4.5.0/js/", "webjars/font-awesome/4.7.0/css/font-awesome.min.css", "/webjars/**"
                , "/resources/**"
                ).permitAll()
                
                .antMatchers("/logy").hasAnyAuthority("ADMIN", "SUPER-USER")
                .antMatchers("/ciselniky", "/novyciselnik", "/zmenaciselniku").hasRole("ADMIN")
                //.antMatchers("/shrnuti","/vysledek","/vstupy","/sluzby").hasAnyAuthority("USER","ADMIN", "SUPER-USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", false)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll();
        //.and().httpBasic();  //přidáno pro vlastní autentizaci
    }
    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        )*/

    // old spring obsolete
    /*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user
                = User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("{noop}JusticeAtos")
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
     */
    // Bcrypt enabled
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(encoder.encode("JusticeAtos"))
                .roles("ADMIN");
    }*/
    // Bcrypt disabled
    /* @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(("{noop}JusticeAtos"))
                .roles("ADMIN");
    }*/
    // pro externí autorizaci
    @Autowired
    private CustomAuthenticationProvider authProvider;

    // pro externí autorizaci
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

}
