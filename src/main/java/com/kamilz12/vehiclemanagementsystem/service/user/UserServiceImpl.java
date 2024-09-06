package com.kamilz12.vehiclemanagementsystem.service.user;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.repository.role.RoleRepository;
import com.kamilz12.vehiclemanagementsystem.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserByUsername(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + userName));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() ->
                new UsernameNotFoundException("User with id: " + userId + " not found: "));
    }

    @Override
    public Long findLoggedUserIdByUsername() {
        return findUserByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName()).getId();
    }

    @Override
    public void save(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        user.setRoles(Collections.singletonList(roleRepository.findRoleByName("ROLE_USER")));

        userRepository.save(user);
        log.info("Role assigned: {}", user.getRoles().get(0));
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findUserByUsername(userName);

        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getAuthority());
            authorities.add(tempAuthority);
        }
        return authorities;
    }
}
