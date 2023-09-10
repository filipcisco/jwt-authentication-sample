package sn.devion.crm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Permissions
 * Gather all permissions of the application
 * Each group should own a set of permissions
 * Whenever a new feature is added, a set of permission,
 * each related to an action performed on this feature, should be added
 * @version 1.0.0
 */
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read", "Can read informations about admins"),
    ADMIN_UPDATE("admin:update", "Can update informations about admins"),
    ADMIN_CREATE("admin:create", "Can create admins"),
    ADMIN_DELETE("admin:delete", "Can delete admins"),
    MANAGER_READ("management:read","Can read informations about managers"),
    MANAGER_UPDATE("management:update", "Can update informations about managers"),
    MANAGER_CREATE("management:create", "Can create managers"),
    MANAGER_DELETE("management:delete", "Can delete managers"),
    REPRESENTATIVE_READ("representative:read","Can read informations about representatives"),
    REPRESENTATIVE_UPDATE("representative:update", "Can update informations about representatives"),
    REPRESENTATIVE_CREATE("representative:create", "Can create representatives"),
    REPRESENTATIVE_DELETE("representative:delete", "Can delete representatives");

    @Getter
    private final String permission;
    @Getter
    private final String description;
}
