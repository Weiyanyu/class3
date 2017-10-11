package top.yeonon.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.Notice;
import top.yeonon.service.ICommentService;
import top.yeonon.util.DateTimeUtil;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.vo.CommentDetailVo;
import top.yeonon.vo.CommentListVo;

import java.util.List;

@Service("commentService")
public class CommentService implements ICommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public ServerResponse addComment(Comment comment) {
        if (comment == null || comment.getCommentDesc() == null) {
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


    @Override
    public ServerResponse<PageInfo> listOrSearchComment(int pageNum, int pageSize, Integer userId, String commentDesc) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> commentList = commentMapper.selectCommentsByUserIdAndDesc(commentDesc, userId);
        List<CommentListVo> commentListVoList = Lists.newArrayList();
        for (Comment comment : commentList) {
            commentListVoList.add(assembleCommentListVo(comment));
        }
        PageInfo result = new PageInfo(commentList);
        result.setList(commentListVoList);
        return ServerResponse.createBySuccess(result);
    }


    @Override
    public ServerResponse<CommentDetailVo> detailComment(Integer commentId) {
        if (commentId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) {
            return ServerResponse.createByErrorMessage("不存在该评论");
        }
        CommentDetailVo commentDetailVo = assembleCommentDetailVo(comment);
        return ServerResponse.createBySuccess(commentDetailVo);

    }

    //backend

    @Override
    public ServerResponse<PageInfo> listByUserIdOrNoticeId(int pageNum, int pageSize, Integer userId, Integer noticeId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> commentList = commentMapper.selectCommentsByUserIdOrNoticeId(userId, noticeId);
        List<CommentListVo> commentListVoList = Lists.newArrayList();
        for (Comment comment : commentList) {
            commentListVoList.add(assembleCommentListVo(comment));
        }
        PageInfo result = new PageInfo(commentList);
        result.setList(commentListVoList);
        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse updateCommentDesc(Comment comment) {
        if (comment == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        Comment updateComment = commentMapper.selectByPrimaryKey(comment.getCommentId());
        if (updateComment == null) {
            return ServerResponse.createBySuccessMessage("不存在该评论，无法修改");
        }
        updateComment.setCommentDesc(comment.getCommentDesc());
        updateComment.setInsertImage(comment.getInsertImage());
        int rowCount = commentMapper.updateByPrimaryKeySelective(updateComment);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("修改失败，服务器异常");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    private CommentListVo assembleCommentListVo(Comment comment) {
        CommentListVo commentListVo = new CommentListVo();
        commentListVo.setUserId(comment.getUserId());
        commentListVo.setCommentId(comment.getCommentId());
        commentListVo.setNoticeId(comment.getNoticeId());
        commentListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        commentListVo.setBrief(getCommentBrief(comment.getCommentDesc()));
        return commentListVo;
    }

    private CommentDetailVo assembleCommentDetailVo(Comment comment) {
        CommentDetailVo commentDetailVo = new CommentDetailVo();
        commentDetailVo.setCommentDesc(comment.getCommentDesc());
        commentDetailVo.setCommentId(comment.getCommentId());
        commentDetailVo.setNoticeId(comment.getNoticeId());
        commentDetailVo.setUserId(comment.getUserId());
        commentDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        commentDetailVo.setCreateTime(DateTimeUtil.dateToStr(comment.getCreateTime()));
        commentDetailVo.setUpdateTime(DateTimeUtil.dateToStr(comment.getUpdateTime()));
        return commentDetailVo;
    }

    private String getCommentBrief(String commentDesc) {
        if (commentDesc.length() >= 10) {
            return commentDesc.substring(0,10);
        }
        return commentDesc;
    }

}
