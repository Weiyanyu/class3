package top.yeonon.controller.front;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("test")
public class test {

    @RequestMapping("/set")
    public void set(HttpServletResponse response, HttpSession session) {
        CookieUtil.writeCookie(response, session.getId());
    }

    @RequestMapping("/get")
    public String get(HttpServletRequest request) {
        return CookieUtil.readCookie(request);
    }
}
