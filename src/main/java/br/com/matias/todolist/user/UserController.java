package br.com.matias.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/test")
    public ResponseEntity<?> create(@RequestBody UserModal userModal) {
        var user = this.userRepository.findByUsername(userModal.getUsername());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErroResponse("Usuário já cadastrado."));
        } if (userModal.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErroResponse("Preencha o nome do usuário"));
        } if (userModal.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErroResponse("Preencha o Username."));
        } else if (userModal.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErroResponse("Preencha a senha do usuário."));
        }

        var passwordHash = BCrypt.withDefaults()
                .hashToString(12, userModal.getPassword()
                        .toCharArray());
        userModal.setPassword(passwordHash);

        var userCreated = this.userRepository.save(userModal);
        return ResponseEntity.status(200).body(userCreated);
    }
}
