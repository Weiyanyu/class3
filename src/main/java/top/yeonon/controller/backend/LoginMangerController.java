package top.yeonon.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class LoginMangerController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login" , method = RequestMethod.POST)
    public ServerResponse<Integer> login(String studentId, String password, HttpSession session) {
        ServerResponse<Integer> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            Integer userId = response.getData();
            ServerResponse checkResponse = userService.checkRole(userId);
            if (checkResponse.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, userId);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，请登录管理员账号");
            }
        }
        return response;
    }
}
