package top.yeonon.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.UserMapper;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;
import top.yeonon.util.MD5Util;
import top.yeonon.util.TokenCache;

import java.util.UUID;

@Service("userService")
public class UserService implements IUserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;


    //登录功能，返回登录信息（密码设置为空），密码是通过MD5加密的
    @Override
    public ServerResponse<User> login(String studentId, String password) {
        int rowCount = userMapper.checkStudentId(studentId);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("登录失败，不存在该学号");
        }


        String MD5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.login(studentId, MD5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("登录失败，密码错误，请检查密码");
        }
        userMapper.updateByPrimaryKeySelective(user);
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user, "登录成功");
    }


    //注册功能，通过填写表单信息，比如邮箱，用户名，学号，密码，头像等等提交，密码会经过MD5加密
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = checkValid(user.getStudentId(),Const.STUDENT_ID);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(0);

        //TODO 这里还有设置头像的功能，因为现在FTP服务器还没有搭建，暂时不设置
        //user.setAvatar(user.getAvatar());
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int rowCount = userMapper.insert(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //TODO 这里比较好的方式是发送邮件，邮件里包含带有TOKEN的URL，比较安全
    //填写学号，在没有登录状态下就可以获得对应的找回密码提示问题
    @Override
    public ServerResponse<String> getQuestion(String studentId) {
        ServerResponse validResponse = this.checkValid(studentId, Const.STUDENT_ID);
        if (validResponse.isSuccess()) {
            //学号不存在
            return ServerResponse.createByErrorMessage("该学号不存在");
        }
        String question = userMapper.selectQuestionByStudentId(studentId);
        if (StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMessage("该用户没有填写找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    //验证用户填写的答案是否和数据库中的一致，成功会返回一个Token，供给前端界面调用
    @Override
    public ServerResponse<String> checkAnswer(String studentId, String question, String answer) {
        int rowCount = userMapper.checkAnswer(studentId, question, answer);
        if (rowCount > 0) {
            //说明问题答案和问题对应，且用户ID也对应
            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + studentId, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }


    //通过拿到的token，用过一系列验证token就可以重设密码了
    @Override
    public ServerResponse<String> forgetResetPassword(String studentId, String newPassword, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        String savedToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + studentId);
        logger.warn("saved Token: " + savedToken);
        if (StringUtils.isBlank(savedToken)) {
            //有可能该student_id的用户并没有申请修改账号密码，也就没有token，这里就防止横向越权了
            return ServerResponse.createByErrorMessage("该用户没有申请修改密码，请不要恶意篡改他人账号信息");
        }

        if (!StringUtils.equals(savedToken, token)) {
            return ServerResponse.createByErrorMessage("token无效或者已经过期");
        }


        ServerResponse validResponse = this.checkValid(studentId, Const.STUDENT_ID);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
        int rowCount = userMapper.updatePasswordByStudentId(studentId, md5Password);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改密码成功，请使用密码重新登录");
        }
        return ServerResponse.createByErrorMessage("修改密码失败，有可能是服务器异常");
    }


    //在登录状态下可以直接通过旧密码来更新新密码，设置完成后强制退出登录
    @Override
    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {
        int rowCount = userMapper.checkPassword(user.getUserId(), MD5Util.MD5EncodeUtf8(oldPassword));
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("原密码错误，请检查后重新输入");
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(newPassword);
        user.setPassword(MD5Password);
        rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("修改密码失败，可能是服务器异常");
        }
        return ServerResponse.createBySuccessMessage("修改密码成功，请重新登录");
    }


    //在登录状态下修改个人信息，但是不能修改学号和用户id以及用户权限
    @Override
    public ServerResponse updateInfo(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
        updateUser.setAvatar(user.getAvatar());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setUserName(user.getUserName());
        updateUser.setEmail(user.getEmail());
        updateUser.setAnswer(user.getAnswer());

        int rowCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }

        return ServerResponse.createBySuccess("更新成功");
    }


    //在未登录的状态下使用此功能，正确的话直接强制用户登录，并返回用户信息
    @Override
    public ServerResponse<User> forceGetInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    //校验学号，邮箱的可用性（没有重复）
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            type = type.toUpperCase();
            if (Const.STUDENT_ID.equals(type)) {
                int rowCount = userMapper.checkStudentId(str);
                if (rowCount > 0) {
                    return ServerResponse.createByErrorMessage("学号已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int rowCount = userMapper.checkEmail(str);
                if (rowCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


    //后台管理员登录,检验该用户的身份（role）是否是管理员
    @Override
    public ServerResponse checkRole(User user) {
        if (user != null && user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }




}
