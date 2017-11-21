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

@RestController
@RequestMapping("/manage/comments/")
public class CommentManageController {



    @Autowired
    private ICommentService commentService;


    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(Integer userId) {
        return commentService.getCommentByUserId(userId);
    }


    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ServerResponse update(@PathVariable("id") Integer commentId, Comment comment) {
        return commentService.updateCommentDesc(commentId ,comment);
    }

}
