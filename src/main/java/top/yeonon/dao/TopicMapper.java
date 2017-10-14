package top.yeonon.dao;

import org.apache.ibatis.annotations.Param;
import top.yeonon.pojo.Topic;

import java.util.List;

public interface TopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);


    int selectTopicByName(String topicName);

    //搜索或者list
    List<Topic> searchOrListAllTopic(@Param("topicName") String topicName);
}