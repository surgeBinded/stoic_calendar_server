package com.cebotariropot.stoiccalendarserver.stoic_calendar_server.service;

import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.dao.UserRepository;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.exceptions.UserAlreadyExistsException;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.DAOUser;
import com.cebotariropot.stoiccalendarserver.stoic_calendar_server.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userDao;
    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public JwtUserDetailsService(final UserRepository userDao, final PasswordEncoder bcryptEncoder) {
        this.userDao = userDao;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.isEmpty()) {
            final var user = userDao.findByUsernameLikeIgnoreCase(username);
            return new User(user.getUsername(), user.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public DAOUser save(UserDTO user) {
        DAOUser newUser = new DAOUser();

        List<DAOUser> userList = userDao.findAll();
        boolean userAlreadyExists = userList.stream().anyMatch(daoUser -> daoUser.getUsername().equals(user.getUsername()));
        if (userAlreadyExists) {
            throw new UserAlreadyExistsException("User with the username " + user.getUsername() + " already exists.");
        }

        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userDao.save(newUser);
    }
}