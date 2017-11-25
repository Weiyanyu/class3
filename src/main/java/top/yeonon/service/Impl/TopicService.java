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
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.dao.TopicMapper;
import top.yeonon.pojo.Topic;
import top.yeonon.service.ITopicService;
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


    /**
     *  添加主题，比较简单，没什么好解释的
     * @param topic
     * @return
     */
    @Override
    public ServerResponse<String> addTopic(Topic topic) {

        if (topic == null || StringUtils.isBlank(topic.getName())) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        int rowCount = topicMapper.selectTopicByName(topic.getName());
        if (rowCount > 0) {
            return ServerResponse.createByErrorMessage("该主题已存在，此操作无效");
        }
        Topic newTopic = new Topic();
        newTopic.setName(topic.getName());
        newTopic.setStatus(topic.getStatus());
        newTopic.setDescription(topic.getDescription());
        rowCount = topicMapper.insert(newTopic);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("添加失败，服务器异常");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }


    /**
     *
     * 批量删除，传入id序列，以逗号分隔，所以要解析一下
     *
     * @param topicIds
     * @return
     */
    @Override
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


    /**
     * 这里不太适合递归的删除，因为这些数据的类型删除方式是不一样的，删除方法也不一样，不满足递归的条件，
     * @param topicId
     * @throws Exception
     */
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


    /**
     * 搜索或者返回整个列表，因为这两个功能是包含关系，重复代码很多，所以就把他们合并了，根据传入的参数是否为null判断
     * 返回的内容
     * @param topicName
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo> searchOrListAllTopic(String topicName, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.TopicOrderBy.ID_ASC_DESC.contains(orderBy) || Const.TopicOrderBy.NAME_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Topic> topicList = topicMapper.searchOrListAllTopic(topicName);
        List<TopicListVo> topicListVoList = Lists.newArrayList();
        for (Topic topic : topicList) {
            topicListVoList.add(assembleTopicListVo(topic));
        }
        PageInfo result = new PageInfo(topicList);
        result.setList(topicListVoList);
        return ServerResponse.createBySuccess(result);
    }


    /**
     * 更新，没什么好说的，和其他差不多
     * @param topicId
     * @param topic
     * @return
     */
    @Override
    public ServerResponse updateTopic(Integer topicId, Topic topic) {
        if (topic == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Topic updateTopic = topicMapper.selectByPrimaryKey(topicId);
        if (updateTopic == null) {
            return ServerResponse.createBySuccessMessage("找不到该主题，更新失败");
        }

        updateTopic.setStatus(topic.getStatus());
        updateTopic.setName(topic.getName());
        updateTopic.setDescription(topic.getDescription());

        int rowCount = topicMapper.updateByPrimaryKey(updateTopic);
        if (rowCount <= 0) {
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

    /**
     * 组装视图对象
     * @param topic
     * @return
     */
    private TopicListVo assembleTopicListVo(Topic topic) {
        TopicListVo topicListVo = new TopicListVo();
        topicListVo.setId(topic.getId());
        topicListVo.setStatus(topic.getStatus());
        topicListVo.setTopicName(topic.getName());
        return topicListVo;
    }

}
