package top.yeonon.dao;

import top.yeonon.pojo.Notice;

import java.util.List;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);



    String selectNameById(Integer id);

    List<Integer> selectNoticesIdsByTopicId(Integer topicId);

    int checkTitle(String noticeTitle);

    int checkById(Integer noticeId);

    List<Notice> selectNoticesByTopicId(Integer topicId);

    List<Notice> selectNoticesByNoticeTitle(String noticeTitle);

    List<Notice> selectNoticesByUserId(Integer userId);
}