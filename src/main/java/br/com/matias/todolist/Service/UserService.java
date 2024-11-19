package br.com.matias.todolist.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.Response.ErroResponse;
import br.com.matias.todolist.repository.IUserRepository;
import br.com.matias.todolist.modal.DTO.UserDTO;
import br.com.matias.todolist.modal.UserModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public UserDTO createUser(UserModal userModal) {
        if (validaDadosDeCriacaoDoUsuario(userModal) == null) {
            String passwordHash = BCrypt.withDefaults().hashToString(12, userModal
                    .getPassword().toCharArray());
            userModal.setPassword(passwordHash);

            UserModal user = userRepository.save(userModal);

            return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail());
        }
        return null;
    }

    public Boolean deletaUsuarioDoBanco(UUID userId) {
        Optional<UserModal> user = userRepository.findById(userId);

        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    public List<UserDTO> findUsers() {
        List<UserModal> usersList = userRepository.findAll();
        List<UserDTO> usersDTOList = new ArrayList<>();
        usersList.forEach(user -> usersDTOList.add(new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail())));
        return usersDTOList;
    }

    public Object atualizaDadosDoUsuario(UUID userID, UserModal userModal) {
        Optional<UserModal> user = userRepository.findById(userID);

        if (user.isPresent()) {
            UserModal existingUser = user.get();

            if (userModal.getName() != null && userModal.getName().isEmpty()) {
                return new ErroResponse("Informe o nome do usuário.");
            }
            if (userModal.getUsername() != null && userModal.getUsername().isEmpty()) {
                return new ErroResponse("Informe o novo username.");
            }
            if (userModal.getPassword() != null && userModal.getPassword().isEmpty()) {
                return new ErroResponse("Informe a nova senha.");
            }
            if (userModal.getEmail() != null && userModal.getEmail().isEmpty()) {
                return new ErroResponse("Informe o novo e-mail.");
            }

            if (userModal.getEmail() != null) {
                UserModal emailExists = userRepository.findByEmail(userModal.getEmail());
                if (emailExists != null && !emailExists.getId().equals(userID)) {
                    return new ErroResponse("O e-mail já está cadastrado no Banco de Dados.");
                }
            }

            if (userModal.getName() != null) {
                existingUser.setName(userModal.getName());
            }
            if (userModal.getUsername() != null) {
                existingUser.setUsername(userModal.getUsername());
            }
            if (userModal.getPassword() != null) {
                String passwordHash = BCrypt.withDefaults().hashToString(12, userModal.getPassword().toCharArray());
                existingUser.setPassword(passwordHash);
            }
            if (userModal.getEmail() != null) {
                existingUser.setEmail(userModal.getEmail());
            }


            userRepository.save(existingUser);

            return new UserDTO(
                    existingUser.getId(),
                    existingUser.getUsername(),
                    existingUser.getName(),
                    existingUser.getEmail()
            );
        }
        return new ErroResponse("Usuário não encontrado.");
    }
}