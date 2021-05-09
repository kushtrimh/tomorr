package com.kushtrimh.tomorr.user;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.generator.IDGenerator;
import com.kushtrimh.tomorr.user.repository.UserRepository;
import com.kushtrimh.tomorr.user.service.DefaultUserService;
import com.kushtrimh.tomorr.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private IDGenerator idGenerator;

    private UserService userService;

    @BeforeEach
    public void init() {
        idGenerator = new IDGenerator();
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
    public void save_WhenUserIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            userService.save(null);
        });
    }

    @Test
    public void save_WhenUserIsValid_SaveSuccessfully() {
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

    public void compareUserRecordAndUser(AppUserRecord record, User user) {
        assertEquals(record.getAddress(), user.address());
        assertEquals(record.getType(), user.type().name());
    }

    public AppUserRecord newUserRecord(String id) {
        var userRecord = new AppUserRecord();
        userRecord.setId(id);
        userRecord.setAddress(id + "@tomorrtest.com");
        userRecord.setType("EMAIL");
        return userRecord;
    }
}
