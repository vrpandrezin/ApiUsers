package br.com.matias.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

        @Autowired
        private IUserRepository userRepository;

        public ErroResponse validaDadosDeCriacaoDoUsuario(UserModal userModal) {

            if (userRepository.findByUsername(userModal.getUsername()) != null) {
                return new ErroResponse("Usuário já cadastrado.");
            }
            if (userModal.getName() == null || userModal.getName().isEmpty()) {
                return new ErroResponse("Preencha o nome do usuário.");
            }
            if (userModal.getUsername() == null || userModal.getUsername().isEmpty()) {
                return new ErroResponse("Preencha o username.");
            }
            if (userModal.getPassword() == null || userModal.getPassword().isEmpty()) {
                return new ErroResponse("Preencha a senha.");
            }
            if (userModal.getEmail() == null || userModal.getEmail().isEmpty()) {
                return new ErroResponse("Preencha o e-mail.");
            }
            return null;
        }

        public UserModal createUser(UserModal userModal) {
            String passwordHash = BCrypt.withDefaults().hashToString(12, userModal
                    .getPassword().toCharArray());
            userModal.setPassword(passwordHash);
            return userRepository.save(userModal);
        }

        public Optional<UserModal> deletaUsuarioDoBanco(UUID userId) {
            var idUsuario = userRepository.findById(userId);
            idUsuario.ifPresent(userRepository::delete);
            return idUsuario;
        }
}