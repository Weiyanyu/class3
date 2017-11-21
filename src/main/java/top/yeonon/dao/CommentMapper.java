package top.yeonon.dao;

import org.apache.ibatis.annotations.Param;
import top.yeonon.pojo.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Integer> selectCommentsIdsByNoticeId(Integer noticeId);

    List<Comment> selectCommentsByUserId(Integer userId);

    List<Comment> selectCommentsByUserIdOrNoticeId(@Param("userId") Integer userId, @Param("noticeId") Integer noticeId);

    List<Comment> selectCommentsByUserIdAndDesc(@Param("commentDesc") String commentDesc, @Param("userId") Integer userId);
}