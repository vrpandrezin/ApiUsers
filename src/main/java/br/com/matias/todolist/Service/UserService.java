package br.com.matias.todolist.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.Repository.IUserRepository;
import br.com.matias.todolist.response.ErroResponse;
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
            return new ErroResponse("Username já cadastrado.");
        }
        if (userRepository.findByEmail(userModal.getEmail()) != null) {
            return new ErroResponse("E-mail já cadastrado.");
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

    public Object createUser(UserModal userModal) {
        ErroResponse erro = validaDadosDeCriacaoDoUsuario(userModal);

        if (erro != null) {
            return erro;
        }
        try {
            String passwordHash = BCrypt.withDefaults().hashToString(12, userModal
                    .getPassword().toCharArray());
            userModal.setPassword(passwordHash);

            if (userModal.getType() == null || userModal.getType().isEmpty()) {
                userModal.setType("USER");
            }

            UserModal user = userRepository.save(userModal);

            return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getType(), user.getEmail());
        } catch (Exception e) {
            return new ErroResponse("Erro ao salvar os dados.");
        }
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
        if (usersList.isEmpty()) {
            throw new RuntimeException("Nenhum usuário cadastrado no banco de dados.");
        }
        usersList.forEach(user -> usersDTOList.add(new UserDTO(user.getId(), user.getName(),
                user.getUsername(), user.getType(), user.getEmail())));
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
                    existingUser.getName(),
                    existingUser.getUsername(),
                    existingUser.getType(),
                    existingUser.getEmail()
            );
        }
        return new ErroResponse("ID não encontrado.");
    }
}