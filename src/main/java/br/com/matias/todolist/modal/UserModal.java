package br.com.matias.todolist.modal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "tb_users")
public class UserModal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String type;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @CreationTimestamp
    private LocalDateTime createdAt;
}