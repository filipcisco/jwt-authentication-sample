package sn.devion.jwtauthenticationsample.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.devion.jwtauthenticationsample.dtos.UserDTO;
import sn.devion.jwtauthenticationsample.entities.User;
import sn.devion.jwtauthenticationsample.repositories.UserRepository;
import sn.devion.jwtauthenticationsample.services.GroupService;
import sn.devion.jwtauthenticationsample.services.UserService;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupService groupService;
    /**
     * Password encoder
     * Used to hash passwords and check if a password matches a hash
     * @see PasswordEncoder
     */
    private final PasswordEncoder passwordEncoder;


    /**
     * Find a user by id
     * @param id user id
     * @return   user
     */
    @Override
    public UserDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    /**
     * Find all users
     * @param index    page index
     * @param size     page size
     * @param sort     sort by
     * @param order    order by
     * @return         page of users
     */
    @Override
    public Page<UserDTO> findAll(int index, int size, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(index, size, Sort.by(direction, sort));
        Page<UserDTO> page = userRepository.findAll(pageRequest).map(this::convertToDTO);
        return new PageImpl<>(page.getContent(), pageRequest, page.getTotalElements());
    }

    /**
     * Create a user
     * @param userDTO userDTO
     * @return        created user
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        val group = groupService.findByName(userDTO.groupName());
        user.setGroup(group);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Update a user
     * @param id      user id
     * @param userDTO userDTO
     * @return        updated user
     */
    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update user fields based on userDTO
            user.setFirstname(userDTO.firstname());
            user.setLastname(userDTO.lastname());
            user.setUsername(userDTO.username());
            /*
             * Those fields should be updated
             * via a dedicated method for each one
             */
            // user.setEmail(userDTO.email());
            // user.setPassword(userDTO.password());
            // user.setEnabled(userDTO.enabled());
            // user.setExpired(userDTO.expired());
            // user.setGroupAuthorities(userDTO.getGroupAuthorities());
            User updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        }
        throw new UsernameNotFoundException("User not found with id: " + id);
    }

    /**
     * Delete a user
     * @param id user id
     */
    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        userRepository.deleteById(user.getId());
    }

    /**
     * Find a user by username
     * @param username  username
     * @return          user
     */
    @Override
    public UserDTO findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    /**
     * Convert a user to a userDTO
     * @param user user
     * @return     userDTO
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isExpired(),
                user.getGroup().getName(),
                user.getGroup().getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority().toLowerCase(Locale.ROOT))
                        .collect(Collectors.joining(","))
        );
    }

    /**
     * Convert a userDTO to a user
     * @param userDTO userDTO
     * @return        user
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstname(userDTO.firstname());
        user.setLastname(userDTO.lastname());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setEnabled(userDTO.enabled());
        user.setExpired(userDTO.expired());
        return user;
    }
}
