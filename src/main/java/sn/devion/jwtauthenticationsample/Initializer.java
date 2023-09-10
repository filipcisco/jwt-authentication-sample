package sn.devion.jwtauthenticationsample;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.devion.jwtauthenticationsample.dtos.GroupDTO;
import sn.devion.jwtauthenticationsample.dtos.UserDTO;
import sn.devion.jwtauthenticationsample.enums.Permission;
import sn.devion.jwtauthenticationsample.services.GroupService;
import sn.devion.jwtauthenticationsample.services.UserService;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This class is used to initialize the database with the super admin group and user
 * if they don't already exist.
 */
@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final GroupService groupService;
    private final UserService userService;

    private static final String SUPER_ADMIN_GROUP_NAME = "Super Admin";
    private static final String SUPER_ADMIN_USERNAME = "admin";

    @Override
    public void run(String... args) {
        initializeSuperAdminGroup();
        initializeSuperAdminUser();
    }

    private void initializeSuperAdminGroup() {
        try {
            groupService.findDTOByName(SUPER_ADMIN_GROUP_NAME);
        } catch (EntityNotFoundException e) {
            Set<String> allPermissions = Arrays.stream(Permission.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            GroupDTO newSuperAdminGroup = new GroupDTO(null, SUPER_ADMIN_GROUP_NAME, "Super Admin Group", allPermissions);
            groupService.create(newSuperAdminGroup);
        }
    }

    private void initializeSuperAdminUser() {
        try {
            userService.findByUsername(SUPER_ADMIN_USERNAME);
        } catch (EntityNotFoundException e) {
            UserDTO newSuperAdminUser = new UserDTO(
                    null,
                    "Admin",
                    "Super",
                    SUPER_ADMIN_USERNAME,
                    "admin.cisco@yopmail.com",
                    "passer123",
                    true,
                    false,
                    SUPER_ADMIN_GROUP_NAME,
                    null
            );
            userService.createUser(newSuperAdminUser);
        }
    }
}
