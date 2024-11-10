package com.apostoli.UnluckyApp.security;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {


    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new User(user.getUsername(), user.getPassword(),user.getRoles().stream()
                                                    .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                                    .collect(Collectors.toList()));
    }

}