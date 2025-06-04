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
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

/**
 * Contrôleur REST permettant la gestion des utilisateurs (admin uniquement).
 * Tous les endpoints sont accessibles via le préfixe /users.
 */
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

    /**
     * Retourne tous les utilisateurs du système.
     * Accessible uniquement par un administrateur.
     *
     * @return liste de tous les utilisateurs ou erreur 404 si vide
     */
    @GetMapping
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> usersDto = mapper.toUserDtos(userService.getAllUsers());
        if (usersDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(JSON_KEY, LIST_ITEM_NOT_FOUND));
        }
        return ResponseEntity.ok(usersDto);
    }

    /**
     * Ajoute un nouvel utilisateur.
     * Accessible uniquement à un administrateur.
     *
     * @param userDto les données de l'utilisateur à ajouter
     * @return utilisateur créé
     */
    @PostMapping
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        User user = userService.addUser(mapper.toUser(userDto));
        return ResponseEntity.ok(mapper.toUserDto(user));
    }

    /**
     * Met à jour la validation d'un utilisateur (champ "verified").
     * Accessible uniquement à un administrateur.
     *
     * @param userDto utilisateur avec la nouvelle valeur de "verified"
     * @return utilisateur modifié ou erreur serveur
     */
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

    /**
     * Supprime un utilisateur par son identifiant.
     * Accessible uniquement par un administrateur.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @return confirmation de suppression
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.jorami.eyeapp.model.Enum.UserRole).ADMIN.name())")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of(JSON_KEY, "Utilisateur supprimé avec succès"));
    }

    /**
     * Récupère les informations de l'utilisateur actuellement connecté.
     *
     * @param token token JWT présent dans l'en-tête Authorization
     * @return utilisateur courant ou erreur 401
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        User user = userService.getCurrentUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not authenticated"));
        }

        UserDto userDto = mapper.toUserDto(user);

        // Patch : si date de naissance manquante, on en met une par défaut (bug connu sur certains comptes)
        if (user.getBirthDate() == null) {
            userDto.setBirthDate(LocalDate.of(2000, 1, 1));
        }

        return ResponseEntity.ok(userDto);
    }

    /**
     * Met à jour le rôle d’un utilisateur.
     *
     * @param userId identifiant de l'utilisateur
     * @param request corps de la requête contenant une clé "role"
     * @return utilisateur avec le nouveau rôle
     */
    @PutMapping("/role/{userId}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String newRoleName = request.get("role");
        if (newRoleName == null || newRoleName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is missing.");
        }

        User updatedUser = userService.updateUserRole(userId, newRoleName);
        UserDto dto = mapper.toUserDto(updatedUser);

        return ResponseEntity.ok(dto);
    }
}
