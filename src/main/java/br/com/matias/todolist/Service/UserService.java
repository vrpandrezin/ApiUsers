package br.com.matias.todolist.Service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.Response.ErroResponse;
import br.com.matias.todolist.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

        @Autowired
        private IUserRepository userRepository;

        public ErroResponse validaDadosDeCriacaoDoUsuario(UserModalService userModalService) {

            if (userRepository.findByUsername(userModalService.getUsername()) != null) {
                return new ErroResponse("Usuário já cadastrado.");
            }
            if (userModalService.getName() == null || userModalService.getName().isEmpty()) {
                return new ErroResponse("Preencha o nome do usuário.");
            }
            if (userModalService.getUsername() == null || userModalService.getUsername().isEmpty()) {
                return new ErroResponse("Preencha o username.");
            }
            if (userModalService.getPassword() == null || userModalService.getPassword().isEmpty()) {
                return new ErroResponse("Preencha a senha.");
            }
            if (userModalService.getEmail() == null || userModalService.getEmail().isEmpty()) {
                return new ErroResponse("Preencha o e-mail.");
            }
            return null;
        }

        public UserModalService createUser(UserModalService userModalService) {
            String passwordHash = BCrypt.withDefaults().hashToString(12, userModalService
                    .getPassword().toCharArray());
            userModalService.setPassword(passwordHash);
            return userRepository.save(userModalService);
        }

        public Optional<UserModalService> deletaUsuarioDoBanco(UUID userId) {
            var idUsuario = userRepository.findById(userId);
            idUsuario.ifPresent(userRepository::delete);
            return idUsuario;
        }
}