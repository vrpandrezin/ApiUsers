package br.com.matias.todolist.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_users")
public class UserModal {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    private String password;
    private String type;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @CreationTimestamp
    private LocalDateTime createdAt;
}