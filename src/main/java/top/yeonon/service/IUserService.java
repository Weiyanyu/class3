package top.yeonon.service;

import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.vo.UserInfoVo;

public interface IUserService {
    ServerResponse<UserInfoVo> login(String studentId, String password);

    ServerResponse checkValid(String str, String type);

    ServerResponse<String> register(User user);

    ServerResponse<String> getQuestion(String studentId);

    ServerResponse<String> checkAnswer(String studentId, String question, String answer);

    ServerResponse<String> forgetResetPassword(String studentId, String newPassword, String token);

    ServerResponse<String> resetPassword(Integer userId, String oldPassword, String newPassword);

    ServerResponse updateInfo(User user);

    ServerResponse<UserInfoVo> getPublicInfo(Integer userId);

    ServerResponse checkRole(Integer userId);
}
