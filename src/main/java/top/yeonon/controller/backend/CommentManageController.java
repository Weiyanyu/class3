package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Comment;

import top.yeonon.service.ICommentService;

import top.yeonon.vo.CommentDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/comments/")
public class CommentManageController {



    @Autowired
    private ICommentService commentService;


    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         Integer userId,
                                         Integer noticeId) {
        return commentService.listByUserIdOrNoticeId(pageNum, pageSize, userId, noticeId);
    }

    @ManagerPermission
    @RequestMapping(value = "{commentId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CommentDetailVo> detail(@PathVariable("commentId") Integer commentId, HttpSession session) {
        return commentService.detailComment(commentId);
    }

    @ManagerPermission
    @RequestMapping(value = "{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse update(@PathVariable("commentId") Integer commentId, Comment comment) {
        return commentService.updateCommentDesc(commentId ,comment);
    }

}
