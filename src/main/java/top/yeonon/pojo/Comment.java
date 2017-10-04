package top.yeonon.pojo;

import java.util.Date;

public class Comment {
    private Integer commentId;

    private Integer userId;

    private Integer noticeId;

    private Date createTime;

    private Date updateTime;

    private String insertImage;

    private String commentDesc;

    public Comment(Integer commentId, Integer userId, Integer noticeId, Date createTime, Date updateTime, String insertImage, String commentDesc) {
        this.commentId = commentId;
        this.userId = userId;
        this.noticeId = noticeId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.insertImage = insertImage;
        this.commentDesc = commentDesc;
    }

    public Comment() {
        super();
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getInsertImage() {
        return insertImage;
    }

    public void setInsertImage(String insertImage) {
        this.insertImage = insertImage == null ? null : insertImage.trim();
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc == null ? null : commentDesc.trim();
    }
}