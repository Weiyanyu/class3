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
public class Comment {
    private Integer id;

    private Integer userId;

    private Integer noticeId;

    private String description;

    private Date createTime;

    private Date updateTime;


}