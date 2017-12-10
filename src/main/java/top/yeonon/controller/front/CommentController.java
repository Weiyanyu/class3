/**
 *   用户评论控制器（前台）
 *   主要是添加，查看的功能
 */
package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.User;
import top.yeonon.service.ICommentService;
import top.yeonon.vo.CommentDetailVo;
import top.yeonon.vo.CommentListVo;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comments/")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    /**
     *
     * 新增一条评论
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        comment.setUserId(user.getUserId());
        return commentService.addComment(comment);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<List<CommentListVo>> getCommentByUserId(Integer userId) {
        return commentService.getCommentByUserId(userId);
    }



}
