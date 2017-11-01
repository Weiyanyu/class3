package top.yeonon.service;

import com.github.pagehelper.PageInfo;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Comment;
import top.yeonon.vo.CommentDetailVo;

public interface ICommentService {

    ServerResponse addComment(Comment comment);

    ServerResponse<PageInfo> listOrSearchComment(int pageNum, int pageSize, Integer userId, String commentDesc);

    ServerResponse<CommentDetailVo> detailComment(Integer commentId);

    ServerResponse<PageInfo> listByUserIdOrNoticeId(int pageNum, int pageSize, Integer userId, Integer noticeId);

    ServerResponse updateCommentDesc(Integer commentId, Comment comment);
}
