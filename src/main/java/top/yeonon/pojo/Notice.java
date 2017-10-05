package top.yeonon.pojo;

import java.util.Date;

public class Notice {
    private Integer noticeId;

    private Integer userId;

    private Integer topicId;

    private String noticeTitle;

    private String noticeDesc;

    private Date createTime;

    private Date updateTime;

    private String mainImage;

    private String subImage;

    public Notice(Integer noticeId, Integer userId, Integer topicId, String noticeTitle, String noticeDesc, Date createTime, Date updateTime, String mainImage, String subImage) {
        this.noticeId = noticeId;
        this.userId = userId;
        this.topicId = topicId;
        this.noticeTitle = noticeTitle;
        this.noticeDesc = noticeDesc;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.mainImage = mainImage;
        this.subImage = subImage;
    }

    public Notice() {
        super();
    }

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
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeDesc() {
        return noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc == null ? null : noticeDesc.trim();
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

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage == null ? null : subImage.trim();
    }
}