package top.yeonon.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private Integer id;

    private Integer userId;

    private Integer topicId;

    private String title;

    private String description;

    private Date createTime;

    private Date updateTime;

}