package ru.jabki.final_todo.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.jabki.final_todo.model.UserCredentials;

@Service
@AllArgsConstructor
public class TodoUserDetailsService implements UserDetailsService {

    private ExternalUserService externalUserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserCredentials userCredentials= externalUserService.userCredentials(userName);
        if (userCredentials == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", userName));
        }
        UserDetails user = User.builder()
                .username(userCredentials.getUsername())
                .password(userCredentials.getPassword())
                .roles(userCredentials.getRole())
                .build();
        return user;
    }
}
