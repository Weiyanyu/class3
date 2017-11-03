package top.yeonon.controller.front;

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
public class LoginController {
    
    @Autowired
    private IUserService userService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<Integer> login(String studentId, String password, HttpSession session) {
        ServerResponse<Integer> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }
}
