package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Comment;

import top.yeonon.service.ICommentService;

import top.yeonon.vo.CommentDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/comment/")
public class CommentManageController {



    @Autowired
    private ICommentService commentService;


    @ManagerPermission
    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         Integer userId,
                                         Integer noticeId) {
        return commentService.listByUserIdOrNoticeId(pageNum, pageSize, userId, noticeId);
    }

    @ManagerPermission
    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<CommentDetailVo> detail(Integer commentId, HttpSession session) {
        return commentService.detailComment(commentId);
    }

    @ManagerPermission
    @RequestMapping("/update")
    @ResponseBody
    public ServerResponse update(Comment comment) {
        return commentService.updateCommentDesc(comment);
    }

}
