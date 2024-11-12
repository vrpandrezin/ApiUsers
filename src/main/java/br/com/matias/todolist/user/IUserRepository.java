package br.com.matias.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModal, UUID> {
    UserModal findByUsername(String username);
}
