package top.yeonon.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.dao.TopicMapper;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.Topic;
import top.yeonon.service.ITopicService;
import top.yeonon.util.DateTimeUtil;
import top.yeonon.vo.TopicDetailVo;
import top.yeonon.vo.TopicListVo;

import java.util.List;


@Service("TopicService")
public class TopicService implements ITopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private CommentMapper commentMapper;

    private Logger logger = LoggerFactory.getLogger(TopicService.class);


    //添加主题
    @Override
    public ServerResponse<String> addTopic(Topic topic) {

        if (topic == null || StringUtils.isBlank(topic.getTopicName())) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        int rowCount = topicMapper.selectTopicByName(topic.getTopicName());
        if (rowCount > 0) {
            return ServerResponse.createByErrorMessage("该主题已存在，此操作无效");
        }
        Topic newTopic = new Topic();
        newTopic.setTopicName(topic.getTopicName());
        newTopic.setTopicStatus(topic.getTopicStatus());
        newTopic.setTopicDesc(topic.getTopicDesc());
        rowCount = topicMapper.insert(newTopic);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("添加失败，服务器异常");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }


    //这里可能会出现其中有一个topic id 错误，就立即停止了，后续无法继续执行的问题。但是在真是环境中，前端会负责给用户展示一个列表
    //让用户选择topic ，所以不会出现ID错误的情况，在这里也仅仅是防止错误，而且如果前端出现一个ID错误，说明数据被截取了，我们不应该删除
    //TODO 应该还可以改进
    @Override
    @Transactional
    public ServerResponse<String> batchDelete(String topicIds) {
        List<String> topicIdList = Splitter.on(",").splitToList(topicIds);
        if (CollectionUtils.isEmpty(topicIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        for (String topicId : topicIdList) {
            Integer IntTopicId = Integer.parseInt(topicId);
            Topic topic = topicMapper.selectByPrimaryKey(IntTopicId);
            if (topic == null) {
                return ServerResponse.createByErrorMessage("该主题不存在，此操作无效");
            }
            try {
                deepDelete(IntTopicId);
            } catch (Exception e) {
                logger.error("删除的时候出现异常");
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("删除失败");
            }
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    private void deepDelete(Integer topicId) throws Exception {

        List<Integer> noticeIdList = noticeMapper.selectNoticesIdsByTopicId(topicId);
        for(Integer noticeId : noticeIdList) {
            List<Integer> commentIdList = commentMapper.selectCommentsIdsByNoticeId(noticeId);
            for (Integer commentId: commentIdList) {
                commentMapper.deleteByPrimaryKey(commentId);
            }
            noticeMapper.deleteByPrimaryKey(noticeId);
        }
        topicMapper.deleteByPrimaryKey(topicId);
    }


    //TODO 未来可能需要排序功能
    @Override
    public ServerResponse<PageInfo> getTopicList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Topic> topicList = topicMapper.selectAll();
        List<TopicListVo> topicListVoList = Lists.newArrayList();
        for (Topic topic : topicList) {
            topicListVoList.add(assembleTopicListVo(topic));
        }
        PageInfo result = new PageInfo(topicList);
        result.setList(topicListVoList);
        return ServerResponse.createBySuccess(result);
    }


    //详情
    @Override
    public ServerResponse<TopicDetailVo> getTopicDetail(Integer topicId) {

        if (topicId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }

        Topic topic = topicMapper.selectByPrimaryKey(topicId);
        if (topic == null) {
            return ServerResponse.createByErrorMessage("改主题不存在");
        }
        TopicDetailVo topicDetailVo = assembleTopicDetailVo(topic);
        return ServerResponse.createBySuccess(topicDetailVo);
    }



    @Override
    public ServerResponse<PageInfo> searchTopic(String topicName, int pageNum, int pageSize) {
        if (StringUtils.isBlank(topicName)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Topic> topicList = topicMapper.selectTopicsByLikeName(topicName);
        List<TopicListVo> topicListVoList = Lists.newArrayList();
        for (Topic topic : topicList) {
            topicListVoList.add(assembleTopicListVo(topic));
        }
        PageInfo result = new PageInfo(topicList);
        result.setList(topicListVoList);
        return ServerResponse.createBySuccess(result);
    }


    @Override
    public ServerResponse updateTopic(Topic topic) {
        if (topic == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Topic updateTopic = topicMapper.selectByPrimaryKey(topic.getTopicId());
        if (updateTopic == null) {
            return ServerResponse.createBySuccessMessage("找不到该主题，更新失败");
        }

        updateTopic.setTopicStatus(topic.getTopicStatus());
        updateTopic.setTopicName(topic.getTopicName());
        updateTopic.setTopicDesc(topic.getTopicDesc());

        int rowCount = topicMapper.updateByPrimaryKey(updateTopic);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

    //装配数据对象
    private TopicListVo assembleTopicListVo(Topic topic) {
        TopicListVo topicListVo = new TopicListVo();
        topicListVo.setId(topic.getTopicId());
        topicListVo.setStatus(topic.getTopicStatus());
        topicListVo.setTopicName(topic.getTopicName());
        return topicListVo;
    }

    private TopicDetailVo assembleTopicDetailVo(Topic topic) {
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        topicDetailVo.setId(topic.getTopicId());
        topicDetailVo.setStatus(topic.getTopicStatus());
        topicDetailVo.setTopicDesc(topic.getTopicDesc());
        topicDetailVo.setTopicName(topic.getTopicName());

        List<Notice> noticeList = noticeMapper.selectNoticesByTopicId(topic.getTopicId());
        topicDetailVo.setAllNotice(noticeList);
        topicDetailVo.setCreateTime(DateTimeUtil.dateToStr(topic.getCreateTime()));
        topicDetailVo.setUpdateTime(DateTimeUtil.dateToStr(topic.getUpdateTime()));
        return topicDetailVo;
    }
}
