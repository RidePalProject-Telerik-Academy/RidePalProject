package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.ridepalapplication.MockHelpers.createAdminMockUser;
import static com.example.ridepalapplication.MockHelpers.createMockUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl service;

    @Test
    void getAll_Should_ReturnAllUsers() {
        User mockUser = createMockUser();
        List<User> allUsers = new ArrayList<>();
        allUsers.add(mockUser);

        Mockito.when(mockRepository.findAll())
                .thenReturn(allUsers);

        List<User> result = service.getAll();
        Assertions.assertEquals(allUsers, result);

        Mockito.verify(mockRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void getById_Should_ReturnUser() {
        User mockUser = createMockUser();

        Mockito.when(mockRepository.findById(mockUser.getId()))
                .thenReturn(Optional.of(mockUser));

        Optional<User> result = service.getById(mockUser.getId());
        Assertions.assertEquals(mockUser, result.orElse(null));

        Mockito.verify(mockRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    void getById_Should_ThrowException_When_IdNotFound() {

        long mockId = Mockito.anyLong();

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.getById(mockId));

        Assertions.assertEquals(String.format("User with id %s not found.", mockId), exception.getMessage());
    }

//    @Test
//    void getByUsername_Should_ReturnUser() {
//        User mockUser = createMockUser();
//
//        Mockito.when(mockRepository.findByUsername(mockUser.getUsername()))
//                .thenReturn(mockUser);
//
//        User result = service.getByUsername(mockUser.getUsername());
//        Assertions.assertEquals(mockUser, result);
//
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .findByUsername(mockUser.getUsername());
//    }

    @Test
    void getByUsername_Should_ThrowException_When_NotFound() {

        String mockUsername = "mockUserName";

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.getByUsername(mockUsername));

        Assertions.assertEquals(String.format("User with %s %s not found.", "username", mockUsername), exception.getMessage());
    }

    @Test
    void createUser_Should_ReturnNewUser_When_UsernameAndEmail_NotExists() {
        User mockUser = createMockUser();

        Mockito.when(mockRepository.findByUsername(Mockito.anyString()))
                .thenReturn(null);

        Mockito.when(mockRepository.findByEmail(Mockito.anyString()))
                .thenReturn(null);

        service.createUser(mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockUser);
    }

//    @Test
//    void createUser_Should_ThrowException_When_UsernameExists() {
//
//        User mockUser = createMockUser();
//        service.createUser(mockUser);
//
//        Mockito.when(mockRepository.findByUsername(mockUser.getUsername()))
//                .thenReturn(mockUser);
//
//        EntityDuplicateException exception = Assertions.assertThrows(
//                EntityDuplicateException.class,
//                () -> service.createUser(mockUser));
//
//        Assertions.assertEquals(String.format("User with %s %s already exists.", "username", mockUser.getUsername()), exception.getMessage());
//    }

    @Test
    void createUser_Should_ThrowException_When_EmailExists() {

        User mockUser = createMockUser();
        service.createUser(mockUser);

        Mockito.when(mockRepository.findByEmail(mockUser.getEmail()))
                .thenReturn(mockUser);

        EntityDuplicateException exception = Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.createUser(mockUser));

        Assertions.assertEquals(String.format("User with %s %s already exists.", "email", mockUser.getEmail()), exception.getMessage());
    }

    @Test
    void updateUser_Should_ReturnUser_When_EmailNotExists_And_SameUser() {
        User loggedUser = createMockUser();
        User userToUpdate = createMockUser();
        userToUpdate.setEmail("test@gmail.com");

        Mockito.when(mockRepository.findByEmail(userToUpdate.getEmail()))
                .thenReturn(null);

        service.updateUser(loggedUser, userToUpdate);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(userToUpdate);
    }

    //TODO: fix logic
    @Test
    void updateUser_Should_ReturnUser_When_EmailNotExists_And_Admin() {
        User mockUser = createMockUser();
        User adminMockUser = createAdminMockUser();

        Mockito.when(mockRepository.save(mockUser))
                .thenReturn(mockUser);

        User result = service.updateUser(adminMockUser, mockUser);
        Assertions.assertEquals(mockUser, result);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockUser);
    }

    @Test
    void updateUser_Should_ThrowException_When_EmailExists() {
        User mockUser = createMockUser();
        User anotherUser = createAdminMockUser();
        anotherUser.setEmail("mock_user@user.com");

        Mockito.when(mockRepository.findByEmail(mockUser.getEmail()))
                .thenReturn(anotherUser);

        EntityDuplicateException exception = Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.updateUser(mockUser, mockUser));

        Assertions.assertEquals(String.format("User with %s %s already exists.", "email", mockUser.getEmail()), exception.getMessage());
    }

    @Test
    void updateUser_Should_ThrowException_When_OtherUser() {
        User mockUser = createMockUser();
        User newMockUser = new User();

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.updateUser(newMockUser, mockUser));

        Assertions.assertEquals(String.format("You are not allowed to %s other users.", "update"), exception.getMessage());
    }


    @Test
    void deleteUser_should_CallRepository_When_SameUser() {
        User mockUser = createMockUser();

        Mockito.when(mockRepository.findById(mockUser.getId()))
                .thenReturn(Optional.of(mockUser));

        service.deleteUser(mockUser, mockUser.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteById(mockUser.getId());
    }

    @Test
    void deleteUser_should_CallRepository_WhenAdmin() {
        User mockUser = createMockUser();
        User adminMockUser = createAdminMockUser();

        Mockito.when(mockRepository.findById(mockUser.getId()))
                .thenReturn(Optional.of(mockUser));

        service.deleteUser(adminMockUser, mockUser.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteById(mockUser.getId());
    }

    @Test
    void deleteUser_Should_ThrowException_When_OtherUser() {
        User mockUser = createMockUser();
        User newMockUser = new User();

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteUser(newMockUser, mockUser.getId()));

        Assertions.assertEquals(String.format("You are not allowed to %s other users.", "delete"), exception.getMessage());
    }
}
