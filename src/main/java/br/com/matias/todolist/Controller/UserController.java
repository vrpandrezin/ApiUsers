package br.com.matias.todolist.controller;

import br.com.matias.todolist.Response.ErroResponse;
import br.com.matias.todolist.Response.StatusResponse;
import br.com.matias.todolist.modal.DTO.UserDTO;
import br.com.matias.todolist.modal.UserModal;
import br.com.matias.todolist.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userList")
    public ResponseEntity<?> listUsers() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse("Listagem de usuários", userService.findUsers()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Não foi possível listar os usuários."));
        }
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> createUser(@RequestBody UserModal userModal) {
        try {
            UserDTO user = userService.createUser(userModal);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new StatusResponse("Usuário cadastrado.", user));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Esse email já foi cadastrado."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Não foi possível cadastrar o usuário."));
        }
    }
    @DeleteMapping("/remUser/{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userID) {
        try {
            if (userService.deletaUsuarioDoBanco(userID)) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse("Usuário deletado.", "O usuário foi excluído."));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse("Erro ao deletar usuário.", "O id informado não existe no Banco de Dados."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Não foi possível deletar o usuário."));
        }
    }

    @PutMapping("/updateUser/{userID}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userID, @RequestBody UserModal userModal) {
        Object response = userService.atualizaDadosDoUsuario(userID, userModal);

        if (response instanceof ErroResponse) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse("Usuário não encontrado."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
