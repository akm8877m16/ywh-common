package ywh.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ywh.common.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    int deleteByUsername(String username);

    User save(User user);

}