package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.User;
import top.yeonon.service.ICommentService;
import top.yeonon.service.IUserService;
import top.yeonon.vo.CommentDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/comment/")
public class CommentManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;


    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         Integer userId,
                                         Integer noticeId,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return commentService.listByUserIdOrNoticeId(pageNum, pageSize, userId, noticeId);
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<CommentDetailVo> detail(Integer commentId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return commentService.detailComment(commentId);
    }

    @RequestMapping("/update")
    @ResponseBody
    public ServerResponse update(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return commentService.updateCommentDesc(comment);
    }

}
