package com.kma.pich.config;

import com.kma.pich.db.MyUserDetailsService;
import com.kma.pich.db.service.UserRepository;
import com.kma.pich.type.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/catalogue", true)
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                    .antMatchers("/products/add",
                            "/products/remove/**",
                            "/orders",
                            "/admin"
                    ).hasAuthority(Permission.ADMIN.name())
                    .antMatchers("/cart",
                            "/cart/**",
                            "/catalogue",
                            "/product/**",
                            "/products/**",
                            "/products-image/**",
                            "/create-order",
                            "/successful-order"
                    ).authenticated()
                .anyRequest().permitAll();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userRepository);
    }
}

