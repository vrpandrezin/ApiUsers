package br.com.matias.todolist.Controller;

import br.com.matias.todolist.Response.ErroResponse;
import br.com.matias.todolist.Response.StatusResponse;
import br.com.matias.todolist.Service.UserService;
import br.com.matias.todolist.modal.DTO.UserDTO;
import br.com.matias.todolist.modal.UserModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userList")
    public ResponseEntity<?> listUsers() {
        try {
            List<UserDTO> users = userService.findUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StatusResponse("Falha ao buscar.",
                            "Nenhum usuário cadastrado no banco de dados."));
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> createUser(@RequestBody UserModal userModal) {
        try {
            Object result = userService.createUser(userModal);

            if (result instanceof ErroResponse) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new StatusResponse("Usuário cadastrado.", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResponse("Ocorreu um erro na requisição."));
        }
    }

    @DeleteMapping("/remUser/{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userID) {
        try {
            if (userService.deletaUsuarioDoBanco(userID)) {
                return ResponseEntity.status(HttpStatus.OK).body(new StatusResponse("Usuário deletado.", "O usuário foi removido do Banco de dados."));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusResponse("Erro ao deletar usuário.", "O id informado não existe no Banco de dados."));
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
