package top.yeonon.pojo;

import java.util.Date;

public class Notice {
    private Integer id;

    private Integer userId;

    private Integer topicId;

    private String title;

    private String description;

    private String mainImage;

    private String subImage;

    private Date createTime;

    private Date updateTime;

    public Notice(Integer id, Integer userId, Integer topicId, String title, String description, String mainImage, String subImage, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.topicId = topicId;
        this.title = title;
        this.description = description;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Notice() {
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

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
}