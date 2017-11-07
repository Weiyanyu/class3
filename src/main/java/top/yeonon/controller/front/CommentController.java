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

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comments/")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    /**
     *
     * @param comment      必填
     * @param session      框架自动填入
     * @return
     * 新增一条评论
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        comment.setUserId(user.getUserId());
        return commentService.addComment(comment);
    }

    /**
     *
     * @param pageNum           默认1， 可选
     * @param pageSize          默认10， 可选
     * @param session           框架填入
     * @return
     * 返回当前用户的所有评论
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return commentService.listOrSearchComment(pageNum, pageSize, user.getUserId(), null);
    }

    /**
     *
     * @param pageNum           默认1， 可选
     * @param pageSize          默认10， 可选
     * @param commentDesc       描述关键词
     * @param session           框架自动填入
     * @return
     * 根据内容关键词搜索评论
     */
    @CustomerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String commentDesc,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return commentService.listOrSearchComment(pageNum, pageSize, user.getUserId(), commentDesc);
    }


    /***
     *
     * @param commentId  必填
     * @return
     * 用过id获取详细内容
     */
    @CustomerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<CommentDetailVo> detail(@PathVariable("id") Integer commentId) {
        return commentService.detailComment(commentId);
    }



}
