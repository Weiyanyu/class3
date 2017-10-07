package top.yeonon.dao;

import org.apache.ibatis.annotations.Param;
import top.yeonon.pojo.Notice;

import java.util.List;
import java.util.zip.CheckedInputStream;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer noticeId);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer noticeId);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);

    List<Integer> selectNoticesIdsByTopicId(Integer topicId);

    int checkTitle(String noticeTitle);

    int checkById(Integer noticeId);

    List<Notice> selectNoticesByTopicId(Integer topicId);


}