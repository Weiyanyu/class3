package top.yeonon.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.dao.UserMapper;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.util.DateTimeUtil;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.vo.CommentDetailVo;
import top.yeonon.vo.NoticeDetailVo;
import top.yeonon.vo.NoticeListVo;

import javax.xml.soap.Detail;
import java.util.List;

@Service("noticeService")
public class NoticeService implements INoticeService {

    private final Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private CommentMapper commentMapper;


    /**
     * 添加通知的service
     */
    @Override
    public ServerResponse addNotice(Notice notice) {
        if (notice == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        int rowCount = noticeMapper.checkTitle(notice.getTitle());
        if (rowCount > 0) {
            return ServerResponse.createByErrorMessage("该标题的通知已存在");
        }
        rowCount = noticeMapper.insert(notice);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("服务器异常");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }


    /**
     * 批量删除Notice,需要级联删除，而且因为数据库没有外键，所以需要自己写删除逻辑，时间复杂度应该是平方级别的，
     * 用到的情况不多，问题不大。
     */
    @Override
    public ServerResponse batchDeleteNotice(String noticeIds) {
        if (StringUtils.isBlank(noticeIds)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }

        List<String> noticeIdList = Splitter.on(",").splitToList(noticeIds);
        try {
            for (String noticeId : noticeIdList) {
                Integer IntNoticeId = Integer.parseInt(noticeId);
                int rowCount = noticeMapper.checkById(IntNoticeId);
                if (rowCount <= 0) {
                    return ServerResponse.createByErrorMessage("该公告不存在，删除失败");
                }
                List<Integer> commentIdList = commentMapper.selectCommentsIdsByNoticeId(IntNoticeId);
                for (Integer commentId : commentIdList) {
                    commentMapper.deleteByPrimaryKey(commentId);
                }
                noticeMapper.deleteByPrimaryKey(IntNoticeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据库异常");
            return ServerResponse.createByErrorMessage("删除失败");
        }

        return ServerResponse.createBySuccessMessage("删除成功");
    }


    /**
     *
     * 这里就是获取Notice列表，因为可能存在很多记录，所以要分页，避免一次请求太多数据
     * 需要注意的是topicId这个参数，这个参数是可以为null的，不传该参数就是返回所有的Notice
     * 这应该是一种比较低级的实现复用的办法（因为是业务逻辑，如果要使用一些设计模式来解决的话，逻辑就比较复杂了，就有些过分了）
     */
    @Override
    public ServerResponse<PageInfo> getNoticeList(int pageNum, int pageSize, Integer topicId, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);

        //排序(可选项)
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.TopicOrderBy.ID_ASC_DESC.contains(orderBy) || Const.TopicOrderBy.NAME_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Notice> noticeList = noticeMapper.selectNoticesByTopicId(topicId);
        List<NoticeListVo> noticeListVoList = Lists.newArrayList();
        for (Notice notice : noticeList) {
            noticeListVoList.add(assembleNoticeListVo(notice));
        }
        PageInfo result = new PageInfo(noticeList);
        result.setList(noticeListVoList);
        return ServerResponse.createBySuccess(result);

    }

    /**
     * 获取某个Notice的详情，包括创建时间等等几乎所有数据库里存在的内容，而返回list的那块，并没有返回如此详细的信息（因为不需要）
     */
    @Override
    public ServerResponse<NoticeDetailVo> getDetail(Integer noticeId) {
        if (noticeId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        Notice notice = noticeMapper.selectByPrimaryKey(noticeId);
        if (notice == null) {
            return ServerResponse.createByErrorMessage("该公告不存在");
        }
        NoticeDetailVo noticeDetailVo = assembleNoticeDetailVo(notice);
        return ServerResponse.createBySuccess(noticeDetailVo);
    }


    /**
     * 目前仅仅实现了一种比较简陋的搜索（利用mysql的模糊查询）
     */
    @Override
    public ServerResponse<PageInfo> searchNotice(int pageNum, int pageSize, String noticeTitle, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.TopicOrderBy.ID_ASC_DESC.contains(orderBy) || Const.TopicOrderBy.NAME_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Notice> noticeList = noticeMapper.selectNoticesByNoticeTitle(noticeTitle);
        List<NoticeListVo> noticeListVoList = Lists.newArrayList();
        for (Notice notice : noticeList) {
            noticeListVoList.add(assembleNoticeListVo(notice));
        }

        PageInfo result = new PageInfo(noticeList);
        result.setList(noticeListVoList);
        return ServerResponse.createBySuccess(result);
    }


    /**
     * 更新Notice，需要传递一个Notice对象，不需要修改的字段可以为空（MyBatis带来的便利）
     */
    @Override
    public ServerResponse updateNotice(Integer noticeId, Notice notice) {
        if (notice == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Notice updateNotice = noticeMapper.selectByPrimaryKey(noticeId);
        if (updateNotice == null) {
            return ServerResponse.createByErrorMessage("不存在该公告，更新失败");
        }
        updateNotice.setTopicId(notice.getTopicId());
        updateNotice.setDescription(notice.getDescription());
        updateNotice.setTitle(notice.getTitle());

        int rowCount = noticeMapper.updateByPrimaryKey(updateNotice);
        if (rowCount <= 0) {
            return ServerResponse.createBySuccessMessage("服务器异常，更新失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }


    /**
     *
     * 返回某个用户的Notice列表，主要是方便用户知道自己干了什么“惊天”的大事！
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse getNoticeListByUserId(Integer userId) {
        List<Notice> notices = noticeMapper.selectNoticesByUserId(userId);
        if (notices == null) {
            return ServerResponse.createByErrorMessage("该用户没有发布任何公告");
        }
        return ServerResponse.createBySuccess(notices);
    }


    /**
     *
     * 构造View Object (视图对象),主要就是返回视图所需要的信息，一般比数据库里的信息更完整且更 “人性化”
     *
     * @param notice
     * @return
     */

    private NoticeListVo assembleNoticeListVo(Notice notice) {
        NoticeListVo noticeListVo = new NoticeListVo();
        noticeListVo.setNoticeId(notice.getId());
        noticeListVo.setNoticeTitle(notice.getTitle());
        noticeListVo.setDescription(notice.getDescription());
        return noticeListVo;
    }


    /**
     *
     * 同上，不过这里组装的是详细信息
     *
     * @param notice
     * @return
     */

    private NoticeDetailVo assembleNoticeDetailVo(Notice notice) {
        NoticeDetailVo noticeDetailVo = new NoticeDetailVo();
        noticeDetailVo.setUserId(notice.getUserId());
        noticeDetailVo.setTopicId(notice.getTopicId());
        noticeDetailVo.setNoticeId(notice.getId());
        noticeDetailVo.setNoticeTitle(notice.getTitle());
        noticeDetailVo.setNoticeDesc(notice.getDescription());

        List<Comment> commentList = commentMapper.selectCommentsByNoticeId(notice.getId());
        List<CommentDetailVo> detailVos = Lists.newArrayList();
        for (Comment comment : commentList) {
            detailVos.add(assembleCommentDetailVo(comment));
        }
        noticeDetailVo.setCommentDetailVoList(detailVos);
        noticeDetailVo.setCreateTime(DateTimeUtil.dateToStr(notice.getCreateTime()));
        noticeDetailVo.setUpdateTime(DateTimeUtil.dateToStr(notice.getUpdateTime()));
        return noticeDetailVo;
    }


    /**
     *
     * 组装该主题下的评论信息
     * 这个方法本不应该放在这里（似乎应该放在commentService里更合适？）
     * 但是一些软件开发原则上的冲突（一个类里不应该调用其他类的方法），我选择了写在这里
     *
     * @param comment
     * @return
     */
    private CommentDetailVo assembleCommentDetailVo(Comment comment) {
        CommentDetailVo commentDetailVo = new CommentDetailVo();
        commentDetailVo.setCommentDesc(comment.getDescription());
        commentDetailVo.setUserId(comment.getUserId());
        commentDetailVo.setCommentId(comment.getId());
        commentDetailVo.setNoticeId(comment.getNoticeId());
        User user = userMapper.selectByPrimaryKey(comment.getUserId());
        commentDetailVo.setUserName(user.getUserName());
        commentDetailVo.setUserAvatar(user.getAvatar());
        commentDetailVo.setCreateTime(DateTimeUtil.dateToStr(comment.getCreateTime()));
        commentDetailVo.setUpdateTime(DateTimeUtil.dateToStr(comment.getUpdateTime()));
        return commentDetailVo;
    }
}
