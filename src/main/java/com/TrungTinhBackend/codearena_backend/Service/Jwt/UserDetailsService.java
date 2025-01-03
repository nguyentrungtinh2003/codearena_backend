package com.TrungTinhBackend.codearena_backend.Service.Jwt;

import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy entity User từ cơ sở dữ liệu
        com.TrungTinhBackend.codearena_backend.Entity.User user = userRepository.findByUsernameAndEnabled(username, true);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Chuyển đổi entity thành UserDetails của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

}
