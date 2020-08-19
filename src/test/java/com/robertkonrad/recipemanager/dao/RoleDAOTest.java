package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Role;
import com.robertkonrad.recipemanager.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserDAO userDAO;

    @Transactional
    @Test
    @Rollback
    public void saveRoleTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        String username = userDAO.saveUser(user);
        Role role = new Role("USER", user);
        roleDAO.saveRole(role);
        Assert.assertEquals("USER", role.getAuthority());
        Assert.assertEquals(user, role.getUser());
    }

    @Transactional
    @Test
    @Rollback
    public void getUserRoleTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        String username = userDAO.saveUser(user);
        Assert.assertNull(roleDAO.getUserRole(username));
        Role role = new Role("USER", user);
        roleDAO.saveRole(role);
        Assert.assertEquals("USER", roleDAO.getUserRole(username));
        Assert.assertNotEquals("ADMIN", roleDAO.getUserRole(username));
    }
}
