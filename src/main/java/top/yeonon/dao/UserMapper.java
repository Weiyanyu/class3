package top.yeonon.dao;

import org.apache.ibatis.annotations.Param;
import top.yeonon.pojo.User;
import top.yeonon.vo.UserInfoVo;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkStudentId(String studentId);

    int checkEmail(String email);

    User login(@Param("studentId") String studentId, @Param("password") String password);

    String selectQuestionByStudentId(@Param("studentId") String studentId);

    int checkAnswer(@Param("studentId")String studentId, @Param("question") String question, @Param("answer") String answer);

    User selectUserByStudentId(String studentId);

    int updatePasswordByStudentId(@Param("studentId") String studentId, @Param("newPassword") String newPassword);

    int checkPassword(@Param("userId") Integer userId, @Param("oldPassword") String oldPassword);

    String selectEmailByStudentId(String studentId);

    int updatePasswordByUserId(@Param("userId") Integer userId, @Param("newPassword") String newPassword);

}