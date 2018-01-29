package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.service.Impl.FileService;
import top.yeonon.util.CookieUtil;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.NoticeDetailVo;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/manage/notices/")
public class NoticeManageController {

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private FileService fileService;

    /**
     *添加notice
     */
    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.addNotice(notice);
    }

    /**
     *上传文件，这里主要就是专门上传notice里的图片文件
     */
    @ManagerPermission
    @RequestMapping(value = "upload_file", method = RequestMethod.POST)
    public ServerResponse uploadFile(@RequestParam("noticeFile")MultipartFile noticeFile,
                                     HttpServletRequest request, HttpSession session) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String remotePath = "img";
        String targetFileName = fileService.upload(noticeFile, path, remotePath, null);
        String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     *批量删除notice，逗号分隔id，当然也可以删除单个
     */
    @ManagerPermission
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse batchDelete(String noticeIds) {
        return noticeService.batchDeleteNotice(noticeIds);
    }

    /**
     *列出某个主题下的所有notice
     */
    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse listByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      Integer topicId, String orderBy) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId, orderBy);
    }

    /**
     *通过标题关键字查找notice
     */
    @ManagerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           String noticeTitle, String orderBy) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle, orderBy);
    }

    /**
     *获取Notice详细信息
     */
    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<NoticeDetailVo> detail(@PathVariable("id") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }

    /**
     *更新某个notice的信息
     */
    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ServerResponse update(@PathVariable("id") Integer noticeId,
                                 Notice notice, HttpServletRequest request) {
        String loginToken = CookieUtil.readCookie(request);
        String userJson = RedisShardedPoolUtil.get(loginToken);
        UserInfoVo currentUser = JsonUtil.stringToObject(userJson, UserInfoVo.class);
        notice.setUserId(currentUser.getUserId());
        return noticeService.updateNotice(noticeId,notice);
    }
}
