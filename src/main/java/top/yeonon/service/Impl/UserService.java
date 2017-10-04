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

    @Override
    public ServerResponse<User> login(String studentId, String password) {
        int rowCount = userMapper.checkStudentId(studentId);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("登录失败，不存在该学号");
        }

        //TODO 这里密码应该要使用MD5加密
        String MD5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.login(studentId, MD5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("登录失败，密码错误，请检查密码");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user, "登录成功");
    }


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


    @Override
    public ServerResponse<User> updateInfo(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createBySuccessMessage("邮箱已存在");
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
        return ServerResponse.createBySuccess(updateUser, "更新个人信息成功");
    }


    @Override
    public ServerResponse<User> forceGetInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

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




}
