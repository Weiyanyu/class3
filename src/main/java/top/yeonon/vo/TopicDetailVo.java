package top.yeonon.vo;

import top.yeonon.pojo.Notice;

import java.util.List;

public class TopicDetailVo {
    private Integer id;
    private String topicName;
    private Integer status;
    private String topicDesc;
    private String createTime;
    private String updateTime;

    private List<Notice> allNotice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
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

    public List<Notice> getAllNotice() {
        return allNotice;
    }

    public void setAllNotice(List<Notice> allNotice) {
        this.allNotice = allNotice;
    }
}
