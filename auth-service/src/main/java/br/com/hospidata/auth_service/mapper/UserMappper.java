package br.com.hospidata.auth_service.mapper;

import br.com.hospidata.auth_service.controller.dto.UserRequest;
import br.com.hospidata.auth_service.controller.dto.UserResponse;
import br.com.hospidata.auth_service.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMappper {

    public User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.name());
        user.setPassword(userRequest.password());
        user.setEmail(userRequest.email());
        user.setRole(userRequest.role());
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getLastUpdatedAt(),
                user.getActive()
        );
    }

    public List<UserResponse> toListResponse(List<User> allUsers) {
        return allUsers.stream()
                .map(this::toResponse)
                .toList();
    }

//    public User toEntity( UserRequestUpdate userRequest) {
//        User user = new User();
//        user.setName(userRequest.name());
//        user.setRole(userRequest.role());
//        user.setEmail(userRequest.email());
//        return user;
//    }

}
