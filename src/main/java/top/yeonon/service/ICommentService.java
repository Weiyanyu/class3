package top.yeonon.service;

import com.github.pagehelper.PageInfo;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Comment;
import top.yeonon.vo.CommentDetailVo;

public interface ICommentService {

    ServerResponse addComment(Comment comment);



    ServerResponse<PageInfo> getCommentByUserId(Integer userId);

    ServerResponse updateCommentDesc(Integer commentId, Comment comment);
}
