package top.yeonon.dao;

import org.apache.ibatis.annotations.Param;
import top.yeonon.pojo.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);


    List<Integer> selectCommentsIdsByNoticeId(Integer noticeId);

    List<Comment> selectCommentsByUserIdOrNoticeId(@Param("userId") Integer userId, @Param("noticeId") Integer noticeId);

    List<Comment> selectCommentsByUserIdAndDesc(@Param("commentDesc") String commentDesc, @Param("userId") Integer userId);


}