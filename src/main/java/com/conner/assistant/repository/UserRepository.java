package com.conner.assistant.repository;


import com.conner.assistant.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
}
