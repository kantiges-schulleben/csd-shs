package com.klnsdr.axon;

import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.service.PermissionService;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "axon api documentation", version = "v1"))
@EnableAsync
public class BackendApplication {
    private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);
    private final PermissionService permissionService;
    private final UserService userService;

    public BackendApplication(PermissionService permissionService, UserService userService) {
        this.permissionService = permissionService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() { // TODO add this to an "Installer App"
        final Permission developerPermission;
        if (!permissionService.existsDevPermission()) {
            logger.error("Developer permission does not exist. Please create it before proceeding.");
            System.exit(1);
            return;
        } else {
            if (permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()).isEmpty()) {
                logger.error("Developer permission does not exist. Please create it before proceeding.");
                System.exit(1);
                return;
            }
            developerPermission = permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()).get();
        }

        final Optional<UserEntity> adminUser = userService.getAdminUser();

        if (adminUser.isEmpty()) {
            logger.warn("No admin user found. Restart the application after creating the first account.");
            return;
        }

        if (permissionService.addPermissionToUser(adminUser.get(), developerPermission)) {
            logger.info("Admin user '{}' has been granted the developer permission.", adminUser.get().getName());
        }
    }
}
