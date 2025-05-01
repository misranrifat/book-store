package com.bookstore.service;

import com.bookstore.dto.UserDto;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        logger.info("Retrieving all users");
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        logger.info("Retrieving user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserDto.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }

    @Transactional
    public void deleteAllUsers() {
        logger.info("Deleting all users");
        userRepository.deleteAll();
        logger.info("All users deleted successfully");
    }
}
