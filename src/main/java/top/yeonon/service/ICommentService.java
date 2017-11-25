package top.yeonon.service;

import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Comment;
import top.yeonon.vo.CommentListVo;

import java.util.List;

public interface ICommentService {

    ServerResponse addComment(Comment comment);

    ServerResponse<List<CommentListVo>> getCommentByUserId(Integer userId);

    ServerResponse updateCommentDesc(Integer commentId, Comment comment);
}
