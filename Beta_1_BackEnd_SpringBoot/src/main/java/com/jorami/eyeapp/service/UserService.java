package com.jorami.eyeapp.service;

import com.jorami.eyeapp.auth.model.AuthenticationRequest;
import com.jorami.eyeapp.model.User;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // GET
    List<User> getAllUsers();

    boolean hasOrganizationsId(List<Long> organizationsId);

    User getUserById(Long userId);

    Optional<User> findUserByEmail(AuthenticationRequest request);

    User findUserByEmailVerified(AuthenticationRequest request);

    // POST
    void saveUser(User user);

    User addUser(User user);

    void validOrganizations(List<Long> organizationsId);

    // PUT
    User editUser(User userSource, User user) throws UnsupportedEncodingException;

    void validateAccountEmail(User user) throws UnsupportedEncodingException;

    // DELETE
    void deleteUser(Long userId);

    User getCurrentUser(String token);

    User updateUserRole(Long userId, String roleName);



}
