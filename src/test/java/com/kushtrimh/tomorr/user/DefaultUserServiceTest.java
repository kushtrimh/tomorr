package com.kushtrimh.tomorr.user;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.generator.IDGenerator;
import com.kushtrimh.tomorr.user.repository.UserRepository;
import com.kushtrimh.tomorr.user.service.DefaultUserService;
import com.kushtrimh.tomorr.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
public class DefaultUserServiceTest {

    @Mock
    private UserRepository<AppUserRecord> userRepository;
    @Mock
    private IDGenerator idGenerator;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new DefaultUserService(userRepository, idGenerator);
    }

    @Test
    public void findById_WhenRecordDoesNotExist_ReturnEmptyOptional() {
        var id = "invalid-id";
        when(userRepository.findById(id)).thenReturn(null);
        Optional<User> user = userService.findById(id);
        assertTrue(user.isEmpty());
    }

    @Test
    public void findById_WhenRecordDoesExist_ReturnUserInsideOptional() {
        var id = "id1";
        var record = newUserRecord(id);
        when(userRepository.findById(id)).thenReturn(record);
        Optional<User> userOpt = userService.findById(id);
        compareUserRecordAndUser(record, userOpt.get());
    }

    @Test
    public void findByAddress_WhenRecordDoesNotExist_ReturnEmptyOptional() {
        var id = "invalid-id";
        var address = id + "@tomorrtest.com";
        when(userRepository.findByAddress(address)).thenReturn(null);
        Optional<User> user = userService.findByAddress(address);
        assertTrue(user.isEmpty());
    }

    @Test
    public void findByAddress_WhenRecordDoesExist_ReturnUserInsideOptional() {
        var id = "id1";
        var address = id + "@tomorrtest.com";
        var record = newUserRecord(id);
        when(userRepository.findByAddress(address)).thenReturn(record);
        Optional<User> userOpt = userService.findByAddress(address);
        compareUserRecordAndUser(record, userOpt.get());
    }

    @Test
    public void findByFollowedArtist_WhenUsersDoNotFollowArtist_ReturnEmptyList() {
        var artistId = "invalid-id";
        when(userRepository.findByFollowedArtist(artistId)).thenReturn(Collections.emptyList());
        var users = userService.findByFollowedArtist(artistId);
        assertTrue(users.isEmpty());
    }

    @Test
    public void findByFollowedArtist_WhenUsersFollowArtist_ReturnListOfUsers() {
        var artistId = "id1";
        var record1 = newUserRecord("id1");
        var record2 = newUserRecord("id2");
        when(userRepository.findByFollowedArtist(artistId)).thenReturn(List.of(record1, record2));
        var users = userService.findByFollowedArtist(artistId);
        assertAll(
                () -> assertEquals(2, users.size()),
                () -> compareUserRecordAndUser(record1, users.get(0)),
                () -> compareUserRecordAndUser(record2, users.get(1))
        );
    }

    @Test
    public void save_WhenUserIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            userService.save(null);
        });
    }

    @Test
    public void save_WhenUserIsValid_SaveSuccessfully() {
        when(idGenerator.generate()).thenReturn("newuser");
        var record = newUserRecord("newuser");
        var user = new User(record.getAddress(), UserType.valueOf(record.getType()));
        userService.save(user);
        verify(userRepository, times(1)).save(argThat(recordToSave -> {
            assertEquals(record.getAddress(), recordToSave.getAddress());
            assertEquals(record.getType(), recordToSave.getType());
            assertFalse(recordToSave.getId().isEmpty());
            return true;
        }));
    }

    @Test
    public void deleteById_WhenIdIsValid_DeleteSuccessfully() {
        var id = "id525";
        userRepository.deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void associate_WhenUserDoesNotExists_CreateNewUserAndSaveAssociation() {
        var artistId = "artist1";
        var userId = "user1";
        var user = new User("user1@tomorrtest.com", UserType.EMAIL);
        var userRecord = newUserRecord(userId);
        when(idGenerator.generate()).thenReturn(userId);
        when(userRepository.findByAddress(user.address())).thenReturn(null);
        when(userRepository.save(userRecord)).thenReturn(userRecord);
        userService.associate(user, artistId);
        verify(userRepository, times(1)).save(userRecord);
        verify(userRepository, times(1)).associate(userRecord.getId(), artistId);
    }

    @Test
    public void associate_WhenAssociationAlreadyExists_ThrowAlreadyExistsException() {
        var artistId = "artist1";
        var userId = "user1";
        var user = new User("user1@tomorrtest.com", UserType.EMAIL);
        var userRecord = newUserRecord(userId);
        when(userRepository.findByAddress(user.address())).thenReturn(userRecord);
        when(userRepository.associationExists(userId, artistId)).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> userService.associate(user, artistId));
    }

    @Test
    public void associate_WhenAssociatingWithUserThatAlreadyExists_SaveAssociation() {
        var artistId = "artist1";
        var userId = "user1";
        var user = new User("user1@tomorrtest.com", UserType.EMAIL);
        var userRecord = newUserRecord(userId);
        when(userRepository.findByAddress(user.address())).thenReturn(userRecord);
        when(userRepository.associationExists(userId, artistId)).thenReturn(false);
        userService.associate(user, artistId);
        verify(userRepository, times(1)).associate(userId, artistId);
    }

    public void compareUserRecordAndUser(AppUserRecord record, User user) {
        assertAll(
                () -> assertEquals(record.getAddress(), user.address()),
                () -> assertEquals(record.getType(), user.type().name())
        );
    }

    public AppUserRecord newUserRecord(String id) {
        var userRecord = new AppUserRecord();
        userRecord.setId(id);
        userRecord.setAddress(id + "@tomorrtest.com");
        userRecord.setType("EMAIL");
        return userRecord;
    }
}
