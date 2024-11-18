package br.com.matias.todolist.repository;

import br.com.matias.todolist.modal.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserModal, UUID> {
    UserModal findByEmail(String email);
}
