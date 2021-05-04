package com.kushtrimh.tomorr.user.repository;

/**
 * @author Kushtrim Hajrizi
 */
public interface UserRepository<T> {

    int count();

    T findById(String id);

    T findByAddress(String address);

    void save(T user);

    void deleteById(String id);
}
