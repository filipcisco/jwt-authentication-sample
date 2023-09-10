package sn.devion.crm.services;


import org.springframework.data.domain.Page;
import sn.devion.crm.dtos.UserDTO;
import java.util.UUID;

public interface UserService {
    UserDTO findById(UUID id);
    Page<UserDTO> findAll(int page, int size, String sort, String order);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
    UserDTO findByUsername(String admin);
}

