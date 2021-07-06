package com.kushtrimh.tomorr.user.service;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.generator.IDGenerator;
import com.kushtrimh.tomorr.spotify.http.DefaultSpotifyHttpClient;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
import com.kushtrimh.tomorr.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultUserService implements UserService {

    private final Logger logger = LoggerFactory.getLogger(DefaultSpotifyHttpClient.class);

    private final UserRepository<AppUserRecord> userRepository;
    private final IDGenerator iDGenerator;

    public DefaultUserService(UserRepository<AppUserRecord> userRepository,
                              IDGenerator idGenerator) {
        this.userRepository = userRepository;
        this.iDGenerator = idGenerator;
    }

    @Transactional
    @Override
    public Optional<User> findById(String id) {
        AppUserRecord record = userRepository.findById(id);
        return getUserOptional(record);
    }

    @Transactional
    @Override
    public Optional<User> findByAddress(String address) {
        AppUserRecord record = userRepository.findByAddress(address);
        return getUserOptional(record);
    }

    @Transactional
    @Override
    public void save(User user) {
        Objects.requireNonNull(user);
        AppUserRecord record = toUserRecord(user);
        userRepository.save(record);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void associate(User user, String artistId) {
        var address = user.address();
        AppUserRecord userRecord = userRepository.findByAddress(address);
        if (userRecord == null) {
            logger.debug("User does not exist, creating new user {}", user);
            userRecord = userRepository.save(toUserRecord(user));
        } else if (userRepository.associationExists(userRecord.getId(), artistId)) {
            throw new AlreadyExistsException();
        }
        userRepository.associate(userRecord.getId(), artistId);
        logger.info("Saved follow for user {} with artist {}", userRecord.getId(), artistId);
    }

    private Optional<User> getUserOptional(AppUserRecord record) {
        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(toUser(record));
    }

    private User toUser(AppUserRecord userRecord) {
        return new User(userRecord.getAddress(),
                UserType.valueOf(userRecord.getType()));
    }

    private AppUserRecord toUserRecord(User user) {
        var userRecord = new AppUserRecord();
        userRecord.setId(iDGenerator.generate());
        userRecord.setType(user.type().name());
        userRecord.setAddress(user.address());
        return userRecord;
    }
}
