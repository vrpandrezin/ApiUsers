package br.com.matias.todolist.Controller;

import br.com.matias.todolist.Response.ErroResponse;
import br.com.matias.todolist.Response.StatusResponse;
import br.com.matias.todolist.Service.UserModalService;
import br.com.matias.todolist.Service.UserService;
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
    public ResponseEntity<?> create(@RequestBody UserModalService userModalService) {
        ErroResponse erro = userService.validaDadosDeCriacaoDoUsuario(userModalService);
        if (erro != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }

        UserModalService userCreated = userService.createUser(userModalService);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @DeleteMapping("/remUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        Optional<UserModalService> userOptional = userService.deletaUsuarioDoBanco(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new StatusResponse("Usuário deletado."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ErroResponse("ID não encontrado."));
        }
    }
}
