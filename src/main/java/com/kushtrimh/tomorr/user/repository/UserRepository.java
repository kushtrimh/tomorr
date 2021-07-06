package com.kushtrimh.tomorr.user.repository;

/**
 * @author Kushtrim Hajrizi
 */
public interface UserRepository<T> {

    int count();

    T findById(String id);

    T findByAddress(String address);

    T save(T user);

    void deleteById(String id);

    void associate(String userId, String artistId);

    boolean associationExists(String userId, String artistId);
}
