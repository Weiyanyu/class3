package top.yeonon.pojo;

import java.util.Date;

public class Topic {
    private Integer topicId;

    private String topicName;

    private Date createTime;

    private Date updateTime;

    public Topic(Integer topicId, String topicName, Date createTime, Date updateTime) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Topic() {
        super();
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName == null ? null : topicName.trim();
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