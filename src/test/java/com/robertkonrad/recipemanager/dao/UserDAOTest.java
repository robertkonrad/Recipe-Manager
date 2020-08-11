package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    @Rollback
    public void saveUserTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        userDAO.saveUser(user);
        List<User> users = userDAO.getUsers();
        Assert.assertEquals(users.get(0).getUsername(), user.getUsername());
        Assert.assertEquals("USER", userDAO.getUserRole(user.getUsername()));
    }

    @Transactional
    @Test
    @Rollback
    public void getUsersTest() {
        List<User> usersBefore = userDAO.getUsers();
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        User user2 = new User("__ss767test6667sz__", "password", 1, "__ss767test6667sz__@a.a");
        userDAO.saveUser(user);
        userDAO.saveUser(user2);
        List<User> usersAfter = userDAO.getUsers();
        Assert.assertEquals(usersAfter.size(), usersBefore.size() + 2);
    }

    @Transactional
    @Test
    @Rollback
    public void usernameAvailableTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        userDAO.saveUser(user);
        Assert.assertEquals(false, userDAO.usernameAvailable(user.getUsername()));
        User fakeUser = userDAO.getUser("__ss767test6667sz__");
        if (fakeUser == null) {
            Assert.assertEquals(true, userDAO.usernameAvailable("__ss767test6667sz__"));
        }
    }

    @Transactional
    @Test
    @Rollback
    public void emailAvailableTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        userDAO.saveUser(user);
        Assert.assertEquals(false, userDAO.emailAvailable(user.getEmail()));
        User fakeUser = userDAO.getUser("__ss767test6667sz__");
        if (fakeUser == null) {
            Assert.assertEquals(true, userDAO.emailAvailable("__ss767test6667sz__@a.a"));
        }
    }

    @Transactional
    @Test
    @Rollback
    public void getUserTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        userDAO.saveUser(user);
        Assert.assertEquals(user, userDAO.getUser(user.getUsername()));
    }

    @Transactional
    @Test
    @Rollback
    public void getUserRoleTest() {
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        userDAO.saveUser(user);
        Assert.assertEquals("USER", userDAO.getUserRole(user.getUsername()));
    }
}
