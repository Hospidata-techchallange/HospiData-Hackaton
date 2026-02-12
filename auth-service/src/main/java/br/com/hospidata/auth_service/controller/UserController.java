package br.com.hospidata.auth_service.controller;

import br.com.hospidata.auth_service.controller.dto.UserRequest;
import br.com.hospidata.auth_service.controller.dto.UserResponse;
import br.com.hospidata.auth_service.entity.enums.Role;
import br.com.hospidata.auth_service.security.aspect.CheckRole;
import br.com.hospidata.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;

    UserController(UserService service){
        this.service = service;
    }

    @GetMapping
    @CheckRole({Role.ADMIN})
    public ResponseEntity<List<UserResponse>> getUsers(){
        var result = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        var result = service.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id){
        var result = service.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity deleteUserById(@PathVariable UUID id){
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
