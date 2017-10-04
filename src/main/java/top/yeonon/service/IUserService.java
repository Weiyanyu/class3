package top.yeonon.service;

import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String studentId, String password);

    ServerResponse checkValid(String str, String type);

    ServerResponse<String> register(User user);

    ServerResponse<String> getQuestion(String studentId);

    ServerResponse<String> checkAnswer(String studentId, String question, String answer);

    ServerResponse<String> forgetResetPassword(String student_id, String newPassword, String token);

    ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword);

    ServerResponse<User> updateInfo(User user);

    ServerResponse<User> forceGetInfo(Integer userId);
}
