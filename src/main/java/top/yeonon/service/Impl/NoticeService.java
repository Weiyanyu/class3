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
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.pojo.Comment;
import top.yeonon.pojo.Notice;
import top.yeonon.service.INoticeService;
import top.yeonon.util.DateTimeUtil;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.vo.NoticeDetailVo;
import top.yeonon.vo.NoticeListVo;

import javax.xml.soap.Detail;
import java.util.List;

@Service("noticeService")
public class NoticeService implements INoticeService {

    private final Logger logger = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ServerResponse addNotice(Notice notice) {
        if (notice == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        int rowCount = noticeMapper.checkTitle(notice.getNoticeTitle());
        if (rowCount > 0) {
            return ServerResponse.createByErrorMessage("该标题的通知已存在");
        }
        //TODO 这里缺少对主图的填充，之后写完文件服务器后再回来写
        if (notice.getMainImage() == null && notice.getSubImage() != null) {
            String subImage[] = notice.getSubImage().split(",");
            if (subImage.length >= 1)
                notice.setMainImage(subImage[0]);
        }

        rowCount = noticeMapper.insert(notice);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("服务器异常");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @Override
    public ServerResponse batchDeleteNotice(String noticeIds) {
        if (StringUtils.isBlank(noticeIds)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        List<String> noticeIdList = Splitter.on(",").splitToList(noticeIds);
        try {
            for (String noticeId : noticeIdList) {
                Integer IntNoticeId = Integer.parseInt(noticeId);
                int rowCount = noticeMapper.checkById(IntNoticeId);
                if (rowCount <= 0) {
                    return ServerResponse.createByErrorMessage("该公告不存在，删除失败");
                }
                List<Integer> commentIdList = commentMapper.selectCommentsByNoticeId(IntNoticeId);
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


    //通过制定topicId查询或者直接查询所有都可以
    @Override
    public ServerResponse<PageInfo> getNoticeList(int pageNum, int pageSize, Integer topicId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> noticeList = noticeMapper.selectNoticesByTopicId(topicId);
        List<NoticeListVo> noticeListVoList = Lists.newArrayList();
        for (Notice notice : noticeList) {
            noticeListVoList.add(assembleNoticeListVo(notice));
        }
        PageInfo result = new PageInfo(noticeList);
        result.setList(noticeListVoList);
        return ServerResponse.createBySuccess(result);

    }


    @Override
    public ServerResponse<NoticeDetailVo> getDetail(Integer noticeId) {
        Notice notice = noticeMapper.selectByPrimaryKey(noticeId);
        if (notice == null) {
            return ServerResponse.createByErrorMessage("该公告不存在");
        }
        NoticeDetailVo noticeDetailVo = assembleNoticeDetailVo(notice);
        return ServerResponse.createBySuccess(noticeDetailVo);
    }


    @Override
    public ServerResponse updateNotice(Notice notice) {
        if (notice == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Notice updateNotice = noticeMapper.selectByPrimaryKey(notice.getNoticeId());
        if (updateNotice == null) {
            return ServerResponse.createByErrorMessage("不存在该公告，更新失败");
        }
        updateNotice.setSubImage(notice.getSubImage());
        if (notice.getSubImage() != null && notice.getMainImage() == null) {
            String[] subImage = notice.getSubImage().split(",");
            if (subImage.length >= 1) {
                updateNotice.setMainImage(subImage[0]);
            }
        }
        updateNotice.setTopicId(notice.getTopicId());
        updateNotice.setNoticeDesc(notice.getNoticeDesc());
        updateNotice.setNoticeTitle(notice.getNoticeTitle());

        int rowCount = noticeMapper.updateByPrimaryKey(updateNotice);
        if (rowCount <= 0) {
            return ServerResponse.createBySuccessMessage("服务器异常，更新失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");

    }

    //装配数据对象（VO）的高复用的方法
    private NoticeListVo assembleNoticeListVo(Notice notice) {
        NoticeListVo noticeListVo = new NoticeListVo();
        noticeListVo.setNoticeId(notice.getNoticeId());
        noticeListVo.setNoticeTitle(notice.getNoticeTitle());
        noticeListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        noticeListVo.setMainImage(notice.getMainImage());
        return noticeListVo;
    }

    private NoticeDetailVo assembleNoticeDetailVo(Notice notice) {
        NoticeDetailVo noticeDetailVo = new NoticeDetailVo();
        noticeDetailVo.setUserId(notice.getUserId());
        noticeDetailVo.setTopicId(notice.getTopicId());
        noticeDetailVo.setNoticeId(notice.getNoticeId());
        noticeDetailVo.setNoticeTitle(notice.getNoticeTitle());
        noticeDetailVo.setNoticeDesc(notice.getNoticeDesc());
        noticeDetailVo.setMainImage(notice.getMainImage());
        noticeDetailVo.setSubImage(notice.getSubImage());
        noticeDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        noticeDetailVo.setCreateTime(DateTimeUtil.dateToStr(notice.getCreateTime()));
        noticeDetailVo.setUpdateTime(DateTimeUtil.dateToStr(notice.getUpdateTime()));
        return noticeDetailVo;
    }



}
