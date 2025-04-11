package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.UserDto;
import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.service.UserService;
import com.jorami.eyeapp.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@ControllerAdvice
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> usersDto = mapper.toUserDtos(userService.getAllUsers());
        if (usersDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(JSON_KEY, LIST_ITEM_NOT_FOUND));
        }
        return ResponseEntity.ok(usersDto);
    }

    @PostMapping
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        User user = userService.addUser(mapper.toUser(userDto));
        return ResponseEntity.ok(mapper.toUserDto(user));
    }

    @PutMapping("/verified/{id}")
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> editVerifiedUser(@RequestBody UserDto userDto) throws UnsupportedEncodingException {
        User userSource = userService.getUserById(userDto.getId());
        if (userSource == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(JSON_KEY, INTERNAL_ERROR));
        }
        UserDto editedUser = mapper.toUserDto(userService.editUser(userSource, mapper.toUser(userDto)));
        return ResponseEntity.ok(editedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of(JSON_KEY, "Utilisateur supprimé avec succès"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        User user = userService.getCurrentUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not authenticated"));
        }

        // S'assurer que `birthDate` est bien présent dans la réponse
        UserDto userDto = mapper.toUserDto(user);
        if (user.getBirthDate() == null) {
            userDto.setBirthDate(LocalDate.of(2000, 1, 1));
        }

        return ResponseEntity.ok(userDto);
    }


}
