package br.com.matias.todolist.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matias.todolist.modal.UserModal;
import br.com.matias.todolist.modal.dto.UserDTO;
import br.com.matias.todolist.repository.IUserRepository;
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

    public List<UserDTO> findUsers() {
        List<UserModal> usersList = userRepository.findAll();
        List<UserDTO> usersDTOList = new ArrayList<>();

        usersList.forEach(user -> usersDTOList.add(new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail())));

        return usersDTOList;
    }

    public UserDTO createUser(UserModal userModal) {
        if (validaDadosDeCriacaoDoUsuario(userModal)) {
            if (verificaExistenciaEmail(userModal)) {
                String passwordHash = BCrypt.withDefaults().hashToString(12, userModal
                        .getPassword().toCharArray());
                userModal.setPassword(passwordHash);

                UserModal user = userRepository.save(userModal);

                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());

                return userDTO;
            }
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

    private Boolean validaDadosDeCriacaoDoUsuario(UserModal userModal) {
        if (userModal.getUsername().isEmpty()) {
            return false;
        }

        if (userModal.getName().isEmpty()) {
            return false;
        }

        if (userModal.getEmail().isEmpty()) {
            return false;
        }

        if (userModal.getPassword().isEmpty()) {
            return false;
        }

        return true;
    }

    private Boolean verificaExistenciaEmail(UserModal userModal) {
        UserModal user = userRepository.findByEmail(userModal.getEmail());

        return user == null;
    }
}