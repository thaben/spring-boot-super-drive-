package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.entities.MyUser;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserRepository;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private HashService hashService;

    public UserService() {
    }

    @Autowired
    public UserService(HashService hashService,UserRepository userRepository) {
        this.hashService =hashService;
        this.userRepository = userRepository;
    }

    public MyUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean createUser(MyUser user) {
        Optional<MyUser> userOptional = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));

        if (userOptional.isPresent()) {
            return false;
        }

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        user.setPassword(hashedPassword);
        user.setSalt(encodedSalt);

        userRepository.save(user);
        logger.info("User Saved with id{}", user.getUserid());

        return true;

    }
}