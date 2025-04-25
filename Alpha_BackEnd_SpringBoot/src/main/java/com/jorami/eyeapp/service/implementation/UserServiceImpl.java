package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.auth.model.AuthenticationRequest;
import com.jorami.eyeapp.auth.service.JwtService;
import com.jorami.eyeapp.model.Address;
import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.repository.UserRepository;
import com.jorami.eyeapp.service.EmailService;
import com.jorami.eyeapp.service.UserService;
import com.jorami.eyeapp.strategy.Implementation.VerifiedAccountEmailStrategy;
import com.jorami.eyeapp.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtUtil;
    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(LIST_ITEM_NOT_FOUND);
        }
    }

    @Override
    public boolean hasOrganizationsId(List<Long> organizationsId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.hasOrganizationsId(((User) auth.getPrincipal()).getId(), organizationsId, organizationsId.size());
    }

    @Override
    public User getUserById(Long userId) {
        try {
            return userRepository.findUserById(userId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(ITEM_NOT_FOUND + " (ID: " + userId + ")");
        }
    }

    @Override
    public Optional<User> findUserByEmail(AuthenticationRequest request) {
        return userRepository.findUserByEmail(request.getEmail());
    }

    @Override
    public User findUserByEmailVerified(AuthenticationRequest request) {
        return userRepository.findUserByEmailVerified(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void validOrganizations(List<Long> organizationsId) {
        if(organizationsId == null || !hasOrganizationsId(organizationsId)) {
            throw new NoSuchElementException(UNAUTHORIZED_ORGANIZATION);
        }
    }

    @Override
    public User editUser(User userSource, User user) throws UnsupportedEncodingException {
        if (user.isValidEmail()) {
            this.validateAccountEmail(user);
        }

        try {
            String[] ignoredProperties = Utils.getNullPropertyNames(user);
            List<String> ignoredList = new ArrayList<>(Arrays.asList(ignoredProperties));
            ignoredList.add("authorities");
            ignoredList.add("role");

            // Vérifie et met à jour la date de naissance si présente
            if (user.getBirthDate() != null) {
                userSource.setBirthDate(user.getBirthDate());
            }

            // Vérifie et met à jour l'adresse
            if (user.getAddress() != null) {
                if (userSource.getAddress() != null) {
                    userSource.getAddress().setStreet(user.getAddress().getStreet());
                    userSource.getAddress().setStreetNumber(user.getAddress().getStreetNumber());
                    userSource.getAddress().setZipCode(user.getAddress().getZipCode());
                    userSource.getAddress().setCity(user.getAddress().getCity());
                    userSource.getAddress().setCountry(user.getAddress().getCountry());
                } else {
                    userSource.setAddress(user.getAddress());
                }
            }

            BeanUtils.copyProperties(user, userSource, ignoredList.toArray(new String[0]));
            return userRepository.save(userSource);

        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException(e.getMessage());
        }
    }
    @Override
    public void validateAccountEmail(User user) throws UnsupportedEncodingException {
        VerifiedAccountEmailStrategy verifiedAccountEmailStrategy = new VerifiedAccountEmailStrategy();
        emailService.sendEmail(user, null, verifiedAccountEmailStrategy);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getCurrentUser(String token) {
        String email = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findUserByEmail(email)
                .orElse(null);
    }

}
