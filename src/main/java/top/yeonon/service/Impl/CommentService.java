package top.yeonon.service.Impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.dao.UserMapper;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.User;
import top.yeonon.service.ICommentService;
import top.yeonon.vo.CommentListVo;

import java.util.List;
import java.util.Map;

@Service("commentService")
public class CommentService implements ICommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Override
    public ServerResponse addComment(Comment comment) {
        if (comment == null || comment.getDescription() == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }

        Notice notice = noticeMapper.selectByPrimaryKey(comment.getNoticeId());
        if (notice == null) {
            return ServerResponse.createByErrorMessage("该公告不存在，评论无效");
        }

        int rowCount = commentMapper.insert(comment);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("评论失败，服务器异常");
        }
        return ServerResponse.createBySuccessMessage("评论成功");
    }



    //backend

    /**
     * 通过用户id获取用户评论
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<List<CommentListVo>> getCommentByUserId(Integer userId) {
        List<Comment> commentList = commentMapper.selectCommentsByUserId(userId);
        List<CommentListVo> commentListVoList = Lists.newArrayList();
        for (Comment comment : commentList) {
            commentListVoList.add(assembleCommentListVo(comment));
        }

        return ServerResponse.createBySuccess(commentListVoList);
    }

    /**
     * 更新评论内容
     * @param commentId
     * @param comment
     * @return
     */
    @Override
    public ServerResponse updateCommentDesc(Integer commentId, Comment comment) {
        if (comment == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        Comment updateComment = commentMapper.selectByPrimaryKey(commentId);
        if (updateComment == null) {
            return ServerResponse.createBySuccessMessage("不存在该评论，无法修改");
        }
        updateComment.setDescription(comment.getDescription());
        int rowCount = commentMapper.updateByPrimaryKeySelective(updateComment);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("修改失败，服务器异常");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    private CommentListVo assembleCommentListVo(Comment comment) {
        CommentListVo listVo = new CommentListVo();
        listVo.setUserId(comment.getUserId());
        listVo.setNoticeId(comment.getNoticeId());
        listVo.setCommentId(comment.getId());
        listVo.setCommentDesc(comment.getDescription());
        User user = userMapper.selectByPrimaryKey(comment.getUserId());
        listVo.setUserName(user.getUserName());
        listVo.setUserAvatar(user.getAvatar());
        String noticeTitle = noticeMapper.selectNameById(comment.getNoticeId());
        listVo.setNoticeTitle(noticeTitle);
        return listVo;
    }
}
