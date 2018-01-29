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
import top.yeonon.util.CookieUtil;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.CommentDetailVo;
import top.yeonon.vo.CommentListVo;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
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
    public ServerResponse add(Comment comment, HttpServletRequest request) {
        String loginToken = CookieUtil.readCookie(request);
        String userJson = RedisShardedPoolUtil.get(loginToken);
        UserInfoVo currentUser = JsonUtil.stringToObject(userJson, UserInfoVo.class);
        comment.setUserId(currentUser.getUserId());
        return commentService.addComment(comment);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<List<CommentListVo>> getCommentByUserId(Integer userId) {
        return commentService.getCommentByUserId(userId);
    }



}
