
package top.yeonon.controller.backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Comment;
import top.yeonon.service.ICommentService;
import top.yeonon.vo.CommentListVo;

import java.util.List;

@RestController
@RequestMapping("/manage/comments/")
public class CommentManageController {

    @Autowired
    private ICommentService commentService;

    /**
     *后台获取notice list
     */
    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<List<CommentListVo>> list(Integer userId) {
        return commentService.getCommentByUserId(userId);
    }

    /**
     *更新notice
     */
    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ServerResponse update(@PathVariable("id") Integer commentId, Comment comment) {
        return commentService.updateCommentDesc(commentId ,comment);
    }
}
