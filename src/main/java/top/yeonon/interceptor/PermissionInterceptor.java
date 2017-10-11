package top.yeonon.interceptor;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String permissionType = "customer";
        //isPass默认应该为true，因为拦截器是作用于整个类的，即使没有注解，仍然会起作用，设置为true就可以防止没有注解的方法不能通过拦截
        boolean isPass = true;
        if (method.getAnnotation(CustomerPermission.class) != null) {
            isPass = isCustomer(request);
            permissionType = "customer";
        }
        if (method.getAnnotation(ManagerPermission.class) != null) {
            isPass = isManager(request);
            permissionType = "manager";
        }

        if (!isPass) {
            Gson gson = new Gson();
            ServerResponse serverResponse = null;
            if (StringUtils.equals(permissionType, "customer")) {
                serverResponse = ServerResponse.createByErrorMessage("用户未登录，请登录");
            }
            if (StringUtils.equals(permissionType, "manager")) {
                serverResponse = ServerResponse.createByErrorMessage("用户未登录或者无权限操作，请登录管理员账号");
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(serverResponse));
        }
        return isPass;
    }

    private boolean isCustomer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return false;
        }
        return true;
    }

    private boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return false;
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return false;
        }
        return true;
    }
}
