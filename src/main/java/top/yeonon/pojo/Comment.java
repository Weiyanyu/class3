package top.yeonon.pojo;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userId;

    private Integer noticeId;

    private String description;

    private Date createTime;

    private Date updateTime;

    private String insertImage;

    public Comment(Integer id, Integer userId, Integer noticeId, String description, Date createTime, Date updateTime, String insertImage) {
        this.id = id;
        this.userId = userId;
        this.noticeId = noticeId;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.insertImage = insertImage;
    }

    public Comment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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