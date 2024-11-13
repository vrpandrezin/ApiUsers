package br.com.matias.todolist.Service;

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
public class UserModalService {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String username;
    private String name;
    private String password;
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Data
    public class ErroResponse {
        private String mensagem;

        public ErroResponse(String mensagem) {

            this.mensagem = mensagem;
        }
    }

    @Data
    public class ErroDelete {
        private String mensagem;

        public ErroDelete(String mensagem) {
            this.mensagem = mensagem;
        }
    }

}
