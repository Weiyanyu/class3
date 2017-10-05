package top.yeonon.pojo;

import java.util.Date;

public class Comment {
    private Integer commentId;

    private Integer userId;

    private Integer noticeId;

    private String commentDesc;

    private Date createTime;

    private Date updateTime;

    private String insertImage;

    public Comment(Integer commentId, Integer userId, Integer noticeId, String commentDesc, Date createTime, Date updateTime, String insertImage) {
        this.commentId = commentId;
        this.userId = userId;
        this.noticeId = noticeId;
        this.commentDesc = commentDesc;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.insertImage = insertImage;
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

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc == null ? null : commentDesc.trim();
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
}