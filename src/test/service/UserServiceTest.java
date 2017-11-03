package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.Impl.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {

//    @Autowired
//    private UserService userService;
//
//    @Test
//    public void loginTest() {
//        ServerResponse<User> response = userService.login("2015010000","admin");
//        User user = response.getData();
//        System.out.println(user.getUserName());
//        System.out.println(user.getPassword());
//    }
}
