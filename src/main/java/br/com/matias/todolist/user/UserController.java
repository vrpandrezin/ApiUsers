package br.com.matias.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<?> create(@RequestBody UserModal userModal) {
        ErroResponse erro = userService.validaDadosDeCriacaoDoUsuario(userModal);
        if (erro != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        UserModal userCreated = userService.createUser(userModal);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @DeleteMapping("/remUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        Optional<UserModal> userOptional = userService.deletaUsuarioDoBanco(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResponse("ID não encontrado."));
        }
    }
}
