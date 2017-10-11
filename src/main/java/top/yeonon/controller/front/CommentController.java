package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.User;
import top.yeonon.service.ICommentService;
import top.yeonon.vo.CommentDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment/")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @CustomerPermission
    @RequestMapping("add_comment")
    @ResponseBody
    public ServerResponse add(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        comment.setUserId(user.getUserId());
        return commentService.addComment(comment);
    }

    @CustomerPermission
    @RequestMapping("list_comment")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return commentService.listOrSearchComment(pageNum, pageSize, user.getUserId(), null);
    }

    @CustomerPermission
    @RequestMapping("search_comment")
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String commentDesc,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return commentService.listOrSearchComment(pageNum, pageSize, user.getUserId(), commentDesc);
    }

    @CustomerPermission
    @RequestMapping("detail_comment")
    @ResponseBody
    public ServerResponse<CommentDetailVo> detail(Integer commentId, HttpSession session) {
        return commentService.detailComment(commentId);
    }



}
