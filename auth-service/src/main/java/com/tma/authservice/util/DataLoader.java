package com.tma.authservice.util;

import com.tma.authservice.entity.User;
import com.tma.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        User user1 = new User();
        user1.setName("admin");
        user1.setUsername("admin");
        user1.setEmail("lmthong98@gmail.com");
        user1.setPassword(passwordEncoder.encode("admin"));
        user1.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

        User user2 = new User();
        user2.setName("thong");
        user2.setUsername("lminhthong");
        user2.setEmail("lmthong98@gmail.com");
        user2.setPassword(passwordEncoder.encode("123"));
        user2.setRoles(Set.of("ROLE_USER"));

        userRepository.saveAll(List.of(user1, user2));
    }
}
