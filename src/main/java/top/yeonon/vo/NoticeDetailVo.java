package top.yeonon.vo;

import top.yeonon.pojo.Comment;

import java.util.List;

public class NoticeDetailVo {
    private Integer noticeId;

    private Integer userId;

    private Integer topicId;

    private String noticeTitle;

    private String noticeDesc;

    private String createTime;

    private String updateTime;

    private List<CommentDetailVo> commentDetailVoList;



    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDesc() {
        return noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    public List<CommentDetailVo> getCommentDetailVoList() {
        return commentDetailVoList;
    }

    public void setCommentDetailVoList(List<CommentDetailVo> commentDetailVoList) {
        this.commentDetailVoList = commentDetailVoList;
    }
}
