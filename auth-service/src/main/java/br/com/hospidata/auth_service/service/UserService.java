package br.com.hospidata.auth_service.service;

import br.com.hospidata.auth_service.controller.dto.UserRequest;
import br.com.hospidata.auth_service.controller.dto.UserResponse;
import br.com.hospidata.auth_service.entity.User;
import br.com.hospidata.auth_service.mapper.UserMappper;
import br.com.hospidata.auth_service.repository.UserRepository;
import br.com.hospidata.auth_service.service.exceptions.DuplicateKeyException;
import br.com.hospidata.auth_service.service.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMappper mapper;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder , UserMappper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {

        if (repository.findByEmail(userRequest.email()).isPresent()) {
            throw new DuplicateKeyException("User", "email", userRequest.email());
        }

        var now = LocalDateTime.now();

        User user = mapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(now);

        return mapper.toResponse(repository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        var users = repository.findAll();
        return mapper.toListResponse(users);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString())));
    }

    @Transactional
    public void  deleteUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        repository.delete(user);
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
