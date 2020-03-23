package com.cobra.authenticationserver;

import com.cobra.authenticationserver.dao.RoleRepository;
import com.cobra.authenticationserver.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationIT {

    @Autowired
    ApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testUserAdded() throws Exception {
        // we've added one user initially, and we want to ensure it is registered
        assertTrue(userRepository.count() > 0);
    }

    @Test
    public void testRolesAdded() throws Exception {
        // since we have defined three roles, we want to assert there are exactly three in the db
        assertTrue(roleRepository.count() == 3);
    }

    @Test
    public void testForAuthManager() throws Exception {
        // verify that our authentication beans have been registered and recognized by the application
        assertTrue(context.getBeanDefinitionCount() > 0);
    }

}
