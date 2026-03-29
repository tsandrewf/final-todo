package ru.jabki.final_todo.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.jabki.final_todo.model.UserCredentials;
import ru.jabki.final_todo.service.ExternalUserService;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private ExternalUserService externalUserService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        UserCredentials userCredentials= externalUserService.userCredentials(userName);
        if (userCredentials == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", userName));
        }

        UserDetails principal = User.builder()
                .username(userCredentials.getUsername())
                .password(Objects.requireNonNull(authentication.getCredentials()).toString())
                .roles(userCredentials.getRole())
                .build();
        return new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}