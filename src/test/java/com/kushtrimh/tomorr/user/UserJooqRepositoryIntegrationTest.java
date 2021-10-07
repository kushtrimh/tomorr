package com.kushtrimh.tomorr.user;

import com.kushtrimh.tomorr.configuration.TestDataSourceConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.user.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("database"), @Tag("integration")})
@ContextConfiguration(classes = TestDataSourceConfiguration.class)
@ExtendWith({TestDatabaseExtension.class})
@JooqTest
public class UserJooqRepositoryIntegrationTest {

    private final UserRepository<AppUserRecord> userRepository;

    @Autowired
    public UserJooqRepositoryIntegrationTest(UserRepository<AppUserRecord> userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void count_ReturnAll() {
        var expectedUsers = 2;
        var usersCount = userRepository.count();
        assertTrue(expectedUsers >= usersCount);
    }

    @Test
    public void findById_WhenIdIsNull_ReturnNull() {
        assertNull(userRepository.findById(null));
    }

    @Test
    public void findById_WhenIdIsEmpty_ReturnNull() {
        assertNull(userRepository.findById(null));
    }

    @Test
    public void findById_WhenUserWithIdDoesNotExist_ReturnNull() {
        assertNull(userRepository.findById("non-existing-user"));
    }

    @Test
    public void findById_WhenUserWithIdExists_ReturnUser() {
        var id = "user1";
        var expectedUser = new AppUserRecord();
        expectedUser.setId(id);
        expectedUser.setAddress("user1@tomorr.com");
        expectedUser.setType("EMAIL");
        var returnedRecord = userRepository.findById(id);
        expectedUser.setCreatedAt(returnedRecord.getCreatedAt());
        assertEquals(expectedUser, returnedRecord);
    }

    @Test
    public void findByAddress_WhenAddressIsNullOrEmptyOrWrong_ReturnNull() {
        assertNull(userRepository.findByAddress(null));
        assertNull(userRepository.findByAddress(""));
        assertNull(userRepository.findByAddress("wrong-address"));
    }

    @Test
    public void findByAddress_WhenUserAddressIsValid_ReturnUser() {
        var address = "user2@tomorr.com";
        var expectedUser = new AppUserRecord();
        expectedUser.setId("user2");
        expectedUser.setAddress(address);
        expectedUser.setType("EMAIL");
        var returnedRecord = userRepository.findByAddress(address);
        expectedUser.setCreatedAt(returnedRecord.getCreatedAt());
        assertEquals(expectedUser, returnedRecord);
    }

    @Test
    public void save_WhenUserIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            userRepository.save(null);
        });
    }

    @Test
    public void save_WhenUserIsValid_SaveSuccessfully() {
        var initialCount = userRepository.count();
        var id = "new-user";
        var user = newUserRecord(id);
        var savedRecord = userRepository.save(user);
        assertEquals(user, savedRecord);
        var countAfterSave = userRepository.count();
        assertNotEquals(initialCount, countAfterSave);

        var returnedUser = userRepository.findById(id);
        user.setCreatedAt(returnedUser.getCreatedAt());
        assertEquals(user, returnedUser);
    }

    @Test
    public void deleteById_WhenUserIdIsNull_DoesNotDelete() {
        assertThatUserIsNotDeleted(null);
    }

    @Test
    public void deleteById_WhenUserIdDoesNotExist_DoesNotDelete() {
        assertThatUserIsNotDeleted("wrong-id");
    }

    @Test
    public void deleteById_WhenUserIdDoesExist_DeleteSuccessfully() {
        var id = "user-to-delete";
        var user = newUserRecord(id);
        userRepository.save(user);
        var initialCount = userRepository.count();

        userRepository.deleteById(id);
        var countAfterDelete = userRepository.count();
        assertNotEquals(initialCount, countAfterDelete);

        assertNull(userRepository.findById(id));
    }

    @Test
    public void associationExists_WhenAssociateDataIsNull_ReturnFalse() {
        assertFalse(userRepository.associationExists(null, "artist-id"));
        assertFalse(userRepository.associationExists("user-id", null));
        assertFalse(userRepository.associationExists(null, null));
    }

    @Test
    public void associationExists_WhenAssociateDoesNotExist_ReturnFalse() {
        assertFalse(userRepository.associationExists("user1", "artist14"));
        assertFalse(userRepository.associationExists("user11", "artist4"));
        assertFalse(userRepository.associationExists("user100", "artist3"));
    }

    @Test
    public void associationExists_WhenAssociateDoesExists_ReturnTrue() {
        assertTrue(userRepository.associationExists("user1", "artist1"));
        assertTrue(userRepository.associationExists("user1", "artist3"));
        assertTrue(userRepository.associationExists("user2", "artist1"));
    }

    @Test
    public void associate_WhenUserIdOrArtistIdIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> userRepository.associate(null, "artist-id"));
        assertThrows(NullPointerException.class, () -> userRepository.associate("user-id", null));
        assertThrows(NullPointerException.class, () -> userRepository.associate(null, null));
    }

    @Test
    public void associate_WhenUserIdAndArtistIdGiven_SaveSuccessfully() {
        var userId = "user2";
        var artistId = "artist2";
        assertFalse(userRepository.associationExists(userId, artistId));
        userRepository.associate(userId, artistId);
        assertTrue(userRepository.associationExists(userId, artistId));
    }

    private void assertThatUserIsNotDeleted(String id) {
        var initialCount = userRepository.count();
        userRepository.deleteById(id);
        var countAfterDelete = userRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    private AppUserRecord newUserRecord(String id) {
        var user = new AppUserRecord();
        user.setId(id);
        user.setAddress("u-" + id + "@tomorr.com");
        user.setType("EMAIL");
        return user;
    }
}
