package br.com.matias.todolist.Repository;

import br.com.matias.todolist.Service.UserModalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserModalService, UUID> {
    UserModalService findByUsername(String username);
    UserModalService findByEmail(String email);
}
