package net.alephdev.lab1.service;

import jakarta.transaction.Transactional;
import net.alephdev.lab1.dto.UserDto;
import net.alephdev.lab1.enums.Role;
import net.alephdev.lab1.models.AdminRequest;
import net.alephdev.lab1.models.User;
import net.alephdev.lab1.repository.AdminRequestRepository;
import net.alephdev.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRequestRepository adminRequestRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AdminRequestRepository adminRequestRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRequestRepository = adminRequestRepository;
    }

    public User register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Неверный пароль");
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с именем пользователя: " + username));
    }

    public AdminRequest createAdminRequest(User user) {
        AdminRequest request = AdminRequest.builder()
                .user(user)
                .status(AdminRequest.Status.PENDING)
                .build();
        return adminRequestRepository.save(request);
    }

    public boolean hasAdminRequest(User user) {
        return adminRequestRepository.findByUser(user).isPresent();
    }

    public List<AdminRequest> getAdminRequests() {
        return adminRequestRepository.findAllByStatus(AdminRequest.Status.PENDING);
    }

    @Transactional
    public void processAdminRequest(Long requestId, boolean approved) {
        AdminRequest request = adminRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Запрос не найден"));

        if (approved) {
            request.setStatus(AdminRequest.Status.APPROVED);
            User user = request.getUser();
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        } else {
            request.setStatus(AdminRequest.Status.REJECTED);
        }

        adminRequestRepository.save(request);
    }
}