package top.yeonon.dao;

import top.yeonon.pojo.Topic;

import java.util.List;

public interface TopicMapper {
    int deleteByPrimaryKey(Integer topicId);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer topicId);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);

    int selectTopicByName(String topicName);

    List<Topic> selectAll();

    List<Topic> selectTopicsByLikeName(String topicName);
}