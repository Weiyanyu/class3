package top.yeonon.service.Impl;

import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.common.ServerResponse;
import top.yeonon.dao.CommentMapper;
import top.yeonon.dao.NoticeMapper;
import top.yeonon.dao.TopicMapper;
import top.yeonon.pojo.Topic;
import top.yeonon.service.ITopicService;
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

    @Override
    public ServerResponse<String> addTopic(String topicName) {
        if (StringUtils.isBlank(topicName)) {
            return ServerResponse.createByErrorMessage("参数错误，请检查后重新输入");
        }
        int rowCount = topicMapper.selectTopicByName(topicName);
        if (rowCount > 0) {
            return ServerResponse.createByErrorMessage("该主题已存在，此操作无效");
        }
        Topic newTopic = new Topic();
        newTopic.setTopicName(topicName);
        newTopic.setTopicStatus(1);
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
            return ServerResponse.createByErrorMessage("参数错误");
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

        List<Integer> noticeIdList = noticeMapper.selectNoticesByTopicId(topicId);
        for(Integer noticeId : noticeIdList) {
            List<Integer> commentIdList = commentMapper.selectCommentsByNoticeId(noticeId);
            for (Integer commentId: commentIdList) {
                commentMapper.deleteByPrimaryKey(commentId);
            }
            noticeMapper.deleteByPrimaryKey(noticeId);
        }
        topicMapper.deleteByPrimaryKey(topicId);
    }
}
