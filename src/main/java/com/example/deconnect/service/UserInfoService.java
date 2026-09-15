package com.example.deconnect.service;

import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepo repository;
    private final PasswordEncoder encoder;

    @Autowired
    @Lazy
    public UserInfoService(UserInfoRepo repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Theres no user with username: " + email));
        return new UserInfoDetails(userInfo);
    }

    public String addUser(UserInfo userInfo) {
        if (repository.findByEmail(userInfo.getEmail()).isPresent()){
            return "Already in use!";
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));

        if (userInfo.getRoles() == null || userInfo.getRoles().isEmpty()){
            userInfo.setRoles("ROLE_USER");
        }

        repository.save(userInfo);
        return "Sikeres Regisztráció!";
    }
}
