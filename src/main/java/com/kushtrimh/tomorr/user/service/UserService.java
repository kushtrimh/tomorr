package com.kushtrimh.tomorr.user.service;

import com.kushtrimh.tomorr.user.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface UserService {

    Optional<User> findById(String id);

    Optional<User> findByAddress(String address);

    List<User> findByFollowedArtist(String artistId);

    void save(User user);

    void deleteById(String id);

    void associate(User user, String artistId);
}
