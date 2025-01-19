package com.agacorporation.demo.config;

import com.agacorporation.demo.domain.Authority;
import com.agacorporation.demo.domain.User;
import com.agacorporation.demo.repository.AuthorityRepository;
import com.agacorporation.demo.repository.UserRepository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class RepositoriesInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public InitializingBean init() {
        return () -> {
            // Check if there are any authorities already in the database
            if (authorityRepository.findAll().isEmpty()) {
                try {
                    // Create roles
                    Authority roleUser = new Authority("ROLE_USER");
                    Authority roleAdmin = new Authority("ROLE_ADMIN");
                    authorityRepository.save(roleUser);
                    authorityRepository.save(roleAdmin);

                    // Create users with Indian names
                    User test = new User("test");
                    test.setAuthorities(new HashSet<>(Arrays.asList(roleUser)));
                    test.setPassword(passwordEncoder.encode("test"));
                    test.setFirstName("Aarav");
                    test.setLastName("Sharma");
                    test.setPhoneNumber("9876543210");
                    test.setEmail("aarav.sharma@example.com");

                    User user = new User("user");
                    user.setAuthorities(new HashSet<>(Arrays.asList(roleUser)));
                    user.setPassword(passwordEncoder.encode("user"));
                    user.setFirstName("Ishaan");
                    user.setLastName("Patel");
                    user.setEmail("ishaan.patel@example.com");
                    user.setPhoneNumber("9876543234");

                    User admin = new User("admin");
                    admin.setFirstName("Priya");
                    admin.setLastName("Reddy");
                    admin.setEmail("priya.reddy@example.com");
                    admin.setPhoneNumber("9876543256");
                    admin.setAuthorities(new HashSet<>(Arrays.asList(roleAdmin)));
                    admin.setPassword(passwordEncoder.encode("admin"));

                    // Save users to the repository
                    userRepository.save(test);
                    userRepository.save(user);
                    userRepository.save(admin);

                } catch (Exception e) {
                    // Log the error for better debugging
                    e.printStackTrace();
                }
            }
        };
    }
}
